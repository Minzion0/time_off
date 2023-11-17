package com.onboarding.time_off.travel.openApi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onboarding.time_off.travel.openApi.model.FestivalInfoBoardRes;
import com.onboarding.time_off.travel.openApi.model.FestivalInfoBoardVo;
import com.onboarding.time_off.travel.openApi.model.FestivalInfoDto;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

@Slf4j
public class OpenApiService {

    private final WebClient webClient;
    private final String travelApiKey;

    @Autowired
    public OpenApiService(@Value("${open.api.travel.key}") String travelApiKey) {
        this.travelApiKey = travelApiKey;

        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(5000));
                    conn.addHandlerLast(new WriteTimeoutHandler(5000));
                });

        this.webClient = WebClient.builder()
                .baseUrl("https://apis.data.go.kr")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();


    }

    public FestivalInfoBoardRes openFestivalInfo(FestivalInfoDto dto) {
        log.info(" key : {}", travelApiKey);
        String json = webClient.get()
                .uri("/B551011/KorService1/areaBasedList1?numOfRows={row}&pageNo={page}&MobileOS=ETC&MobileApp=time_off&_type=json&listYN=Y&arrange={orderBy}&contentTypeId={contentType}&serviceKey={key}", dto.getRow(), dto.getPage(), dto.getOrderBy(), dto.getContentType(), travelApiKey.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<FestivalInfoBoardVo> festivalInfoBoardVo = new ArrayList<>();

        try {
            JsonNode jsonNode = om.readTree(json);
            JsonNode item = jsonNode.get("response").get("body").get("items").get("item");
            int rows = jsonNode.get("response").get("body").get("numOfRows").intValue();
            int page = jsonNode.get("response").get("body").get("pageNo").asInt();
            int totalPage = jsonNode.get("response").get("body").get("totalCount").asInt();

            makeFestivalInfoBoard(rows, item, festivalInfoBoardVo);

            return new FestivalInfoBoardRes(festivalInfoBoardVo, page, totalPage);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public JsonNode FestivalDetailInfo(String contentId,int contentType) {
        String json = webClient.get().uri("/B551011/KorService1/detailIntro1?MobileOS=ETC&MobileApp=time_off&_type=json&contentId={contentId}&contentTypeId={contentType}&serviceKey={key}", contentId, contentType, travelApiKey).retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode node = om.readTree(json);

            return node;

        } catch (Exception e) {

        }
        return null;
    }

    private void makeFestivalInfoBoard(int rows, JsonNode item, List<FestivalInfoBoardVo> festivalInfoBoardVo) {
        for (int i = 0; i < rows; i++) {
            JsonNode base = item.get(i);
            String addr1 = base.get("addr1").asText();
            String addr2 = base.get("addr2").asText();
            Long contentId = base.get("contentid").asLong();
            String title = base.get("title").asText();
            String img = base.get("firstimage2").asText();
            String areaCode = base.get("areacode").asText();
            String createdAt = base.get("modifiedtime").asText();


            if (addr2.length() > 0) {
                addr1 = addr1 + " " + addr2;
            }
            FestivalInfoBoardVo vo = new FestivalInfoBoardVo();
            vo.setAddr(addr1);
            vo.setAreacode(areaCode);
            vo.setTitle(title);
            vo.setContentId(contentId);
            vo.setThumbnailImgUri(img);
            vo.setCreatedAt(createdAt);
            festivalInfoBoardVo.add(vo);
        }
    }
}

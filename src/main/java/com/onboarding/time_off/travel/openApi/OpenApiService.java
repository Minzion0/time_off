package com.onboarding.time_off.travel.openApi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onboarding.time_off.config.entity.MapGpsEntity;
import com.onboarding.time_off.config.repository.MapGpsRepository;
import com.onboarding.time_off.travel.openApi.model.FestivalInfoBoardRes;
import com.onboarding.time_off.travel.openApi.model.FestivalInfoBoardVo;
import com.onboarding.time_off.travel.openApi.model.FestivalInfoDto;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service

@Slf4j
public class OpenApiService {

    private final MapGpsRepository mapGpsRepository;

    private final WebClient webClient;
    private final String travelApiKey;

    @Autowired
    public OpenApiService(@Value("${open.api.travel.key}") String travelApiKey,MapGpsRepository mapGpsRepository) {
        this.travelApiKey = travelApiKey;
        this.mapGpsRepository=mapGpsRepository;

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
        String json = webClient.get().uri("/B551011/KorService1/detailCommon1?MobileOS=ETC&MobileApp=time_off&overviewYN=Y&addrinfoYN=Y&mapinfoYN=Y&defaultYN=Y&firstImageYN=Y&_type=json&contentId={contentId}&contentTypeId={contentType}&serviceKey={key}", contentId, contentType, travelApiKey).retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode node = om.readTree(json);
            JsonNode node1 = node.get("response").get("body").get("items");

            JsonNode items = node.get("response").get("body").get("items").get("item").get(0);

            String homepage = items.get("homepage").asText();
            Document parse = Jsoup.parse(homepage);
            Elements a = parse.body().select("a");
            for (Element element : a) {
                String href = element.attr("href");
                String text = element.attr("title").substring(4).strip();

                System.out.println(text + " = " + href);
            }


            return items;

        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    private void makeFestivalInfoBoard(int rows, JsonNode item, List<FestivalInfoBoardVo> festivalInfoBoardVo) {
        for (int i = 0; i < rows; i++) {
            JsonNode base = item.get(i);
            String addr1 = base.get("addr1").asText();
            String addr2 = base.get("addr2").asText();
            String  contentId = base.get("contentid").asText();
            String title = base.get("title").asText();
            String img = base.get("firstimage2").asText();
            String areaCode = base.get("areacode").asText();
            String createdAt = base.get("modifiedtime").asText();

            double mapx = base.get("mapx").asDouble();
            double mapy = base.get("mapy").asDouble();

            MapGpsEntity mapGps = new MapGpsEntity();
            mapGps.setId(contentId);
            mapGps.setMapX(mapx);
            mapGps.setMapY(mapy);
            mapGpsRepository.save(mapGps);


            String createdSub = createdAt.substring(0,4)+"-"+createdAt.substring(4,8);





            if (addr2.length() > 0) {
                addr1 = addr1 + " " + addr2;
            }
            FestivalInfoBoardVo vo = new FestivalInfoBoardVo();
            vo.setAddr(addr1);
            vo.setAreacode(areaCode);
            vo.setTitle(title);
            vo.setContentId(contentId);
            vo.setThumbnailImgUri(img);
            vo.setCreatedAt(createdSub);
            festivalInfoBoardVo.add(vo);
        }
    }
}

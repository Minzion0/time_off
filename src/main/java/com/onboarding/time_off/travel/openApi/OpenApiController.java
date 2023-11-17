package com.onboarding.time_off.travel.openApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.onboarding.time_off.travel.openApi.model.FestivalInfoBoardRes;
import com.onboarding.time_off.travel.openApi.model.FestivalInfoBoardVo;
import com.onboarding.time_off.travel.openApi.model.FestivalInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time_off")
@RequiredArgsConstructor
public class OpenApiController {

    private final OpenApiService SERVICE;




    @GetMapping("/festival")
    public ResponseEntity<FestivalInfoBoardRes> openFestivalInfo(int row, int page, String orderBy, int contentType){

        FestivalInfoDto dto = FestivalInfoDto.builder()
                .row(row)
                .page(page)
                .orderBy(orderBy)
                .contentType(contentType)
                .build();
        FestivalInfoBoardRes res = SERVICE.openFestivalInfo(dto);
        return res == null? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok().body(res) ;
    }

    @GetMapping("/festival/{contentId}")
    public JsonNode openFestivalDetailInfo(@PathVariable String contentId, @RequestParam int contentType){
       return SERVICE.FestivalDetailInfo(contentId,contentType);
    }

}

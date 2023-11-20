package com.onboarding.time_off.travel.openApi.model;

import lombok.Builder;
import lombok.Data;

/**
 * title 축재명
 *thumbnailImgUri 썸네일 이미지주소
 * areacode 지역코드
 * addr 주소
 * contentid 축제 pk
 * createdAt 생성일자
 */
@Data
public class FestivalInfoBoardVo {
    private String  contentId;
    private String title;
    private String thumbnailImgUri;
    private String addr;
    private String areacode;
    private String createdAt;
}

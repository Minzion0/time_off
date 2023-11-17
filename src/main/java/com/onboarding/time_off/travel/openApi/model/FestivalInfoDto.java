package com.onboarding.time_off.travel.openApi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FestivalInfoDto {
    private int row;
    private int page;
    private String orderBy;
    private int contentType;
}

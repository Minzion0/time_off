package com.onboarding.time_off.travel.openApi.model;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter

public class FestivalInfoBoardRes {
    private List<FestivalInfoBoardVo> list;
    private int page;
    private int totalPage;

    public FestivalInfoBoardRes(List<FestivalInfoBoardVo> list, int page, int totalPage) {
        this.list = list;
        this.page = page;
        this.totalPage = totalPage;
    }
}

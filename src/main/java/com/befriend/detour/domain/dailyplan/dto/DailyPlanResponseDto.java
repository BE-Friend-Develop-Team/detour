package com.befriend.detour.domain.dailyplan.dto;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import lombok.Getter;

@Getter
public class DailyPlanResponseDto {

    private Long scheduleId;
    private Long day;

    public DailyPlanResponseDto(DailyPlan dailyPlan) {
        this.scheduleId = dailyPlan.getSchedule().getId();
        this.day = dailyPlan.getDay();
    }

}

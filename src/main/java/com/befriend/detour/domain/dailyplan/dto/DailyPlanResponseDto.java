package com.befriend.detour.domain.dailyplan.dto;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class DailyPlanResponseDto {

    private Long scheduleId;
    private Long dailyPlanId;
    private Long day;
    private List<MarkerResponseDto> markerList;

    public DailyPlanResponseDto(DailyPlan dailyPlan) {
        this.scheduleId = dailyPlan.getSchedule().getId();
        this.dailyPlanId = dailyPlan.getId();
        this.day = dailyPlan.getDay();
        this.markerList = dailyPlan.getMarkers().stream().map(MarkerResponseDto::new).toList();
    }

}

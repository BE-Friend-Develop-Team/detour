package com.befriend.detour.domain.dailyplan.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DailyPlanRequestDto {

    @NotNull(message = "day값을 지정해주세요.")
    private Long day;

}

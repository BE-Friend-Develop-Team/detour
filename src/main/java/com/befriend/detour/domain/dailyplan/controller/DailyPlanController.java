package com.befriend.detour.domain.dailyplan.controller;

import com.befriend.detour.domain.dailyplan.dto.DailyPlanRequestDto;
import com.befriend.detour.domain.dailyplan.service.DailyPlanService;
import com.befriend.detour.global.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DailyPlanController {

    private final DailyPlanService dailyPlanService;

    @PostMapping("/schedules/{scheduleId}/dailyPlans")
    public ResponseEntity<CommonResponseDto> createDailyPlan(@PathVariable(name = "scheduleId") Long scheduleId,
                                                             @RequestBody DailyPlanRequestDto dailyPlanRequestDto) {
        dailyPlanService.createDailyPlan(scheduleId, dailyPlanRequestDto);

        return new ResponseEntity<>(new CommonResponseDto<>(201, "데일리 플랜 생성에 성공하였습니다. 🎉", null), HttpStatus.CREATED);
    }

}

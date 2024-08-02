package com.befriend.detour.domain.dailyplan.controller;

import com.befriend.detour.domain.dailyplan.dto.DailyPlanRequestDto;
import com.befriend.detour.domain.dailyplan.dto.DailyPlanResponseDto;
import com.befriend.detour.domain.dailyplan.service.DailyPlanService;
import com.befriend.detour.global.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DailyPlanController {

    private final DailyPlanService dailyPlanService;

    @PostMapping("/schedules/{scheduleId}/dailyplans")
    public ResponseEntity<CommonResponseDto> createDailyPlan(@PathVariable(name = "scheduleId") Long scheduleId,
                                                             @RequestBody DailyPlanRequestDto dailyPlanRequestDto) {
        DailyPlanResponseDto dailyPlanResponseDto = dailyPlanService.createDailyPlan(scheduleId, dailyPlanRequestDto);

        return new ResponseEntity<>(new CommonResponseDto<>(201, "데일리 플랜 생성에 성공하였습니다. 🎉", dailyPlanResponseDto), HttpStatus.CREATED);
    }

    @GetMapping("/schedules/{scheduleId}/dailyplans")
    public ResponseEntity<CommonResponseDto<List<DailyPlanResponseDto>>> getDailyPlansByScheduleId(@PathVariable(name = "scheduleId") Long scheduleId) {
        List<DailyPlanResponseDto> responseDtoList = dailyPlanService.getDailyPlansByScheduleId(scheduleId);

        return ResponseEntity.ok(new CommonResponseDto<>(200, "해당 일정에 대한 데일리 플랜 목록 조회에 성공하였습니다. 🎉", responseDtoList));
    }

    @GetMapping("/dailyplans/{dailyPlanId}")
    public ResponseEntity<CommonResponseDto<DailyPlanResponseDto>> getDailyPlan(@PathVariable(name = "dailyPlanId") Long dailyPlanId) {
        DailyPlanResponseDto responseDto = dailyPlanService.getDailyPlan(dailyPlanId);

        return ResponseEntity.ok(new CommonResponseDto<>(200, "데일리 플랜 단건 조회에 성공하였습니다. 🎉", responseDto));
    }

}

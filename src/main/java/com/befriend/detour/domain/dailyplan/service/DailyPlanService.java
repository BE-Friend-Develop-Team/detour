package com.befriend.detour.domain.dailyplan.service;

import com.befriend.detour.domain.dailyplan.dto.DailyPlanRequestDto;
import com.befriend.detour.domain.dailyplan.dto.DailyPlanResponseDto;
import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.dailyplan.repository.DailyPlanRepository;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyPlanService {

    private final DailyPlanRepository dailyPlanRepository;
    private final ScheduleService scheduleService;

    @Transactional
    public void createDailyPlan(Long scheduleId, DailyPlanRequestDto dailyPlanRequestDto) {

        Schedule checkSchedule = scheduleService.findById(scheduleId);

        DailyPlan dailyPlan = new DailyPlan(checkSchedule, dailyPlanRequestDto);
        dailyPlanRepository.save(dailyPlan);
    }

    @Transactional(readOnly = true)
    public List<DailyPlanResponseDto> getDailyPlansByScheduleId(Long scheduleId) {

        Schedule checkSchedule = scheduleService.findById(scheduleId);
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAllByScheduleOrderByDay(checkSchedule);

        List<DailyPlanResponseDto> responseDtoList = new ArrayList<>();
        for (DailyPlan dailyPlan : dailyPlanList) {
            DailyPlanResponseDto responseDto = new DailyPlanResponseDto(dailyPlan);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Transactional(readOnly = true)
    public DailyPlanResponseDto getDailyPlan(Long dailyPlanId) {
        DailyPlan checkDailyPlan = findDailyPlanById(dailyPlanId);

        return new DailyPlanResponseDto(checkDailyPlan);
    }

    // dailyPlanId로 데일리플랜 찾기
    public DailyPlan findDailyPlanById(Long dailyPlanId) {
        return dailyPlanRepository.findById(dailyPlanId).orElseThrow(() ->
                new CustomException(ErrorCode.DAILY_PLAN_NOT_FOUND));
    }
}

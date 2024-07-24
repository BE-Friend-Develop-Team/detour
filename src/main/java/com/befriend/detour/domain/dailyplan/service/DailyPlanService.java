package com.befriend.detour.domain.dailyplan.service;

import com.befriend.detour.domain.dailyplan.dto.DailyPlanRequestDto;
import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.dailyplan.repository.DailyPlanRepository;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyPlanService {

    private final DailyPlanRepository dailyPlanRepository;
    private final ScheduleService scheduleService;

    public void createDailyPlan(Long scheduleId, DailyPlanRequestDto dailyPlanRequestDto) {

        Schedule checkSchedule = scheduleService.findById(scheduleId);

        DailyPlan dailyPlan = new DailyPlan(checkSchedule, dailyPlanRequestDto);
        dailyPlanRepository.save(dailyPlan);
    }

}

package com.befriend.detour.domain.dailyplan.repository;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {
    List<DailyPlan> findAllByScheduleOrderByDay(Schedule checkSchedule);
}

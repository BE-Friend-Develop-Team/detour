package com.befriend.detour.domain.dailyplan.repository;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {
}

package com.befriend.detour.domain.dailyplan.entity;

import com.befriend.detour.domain.dailyplan.dto.DailyPlanRequestDto;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "daily_plans")
public class DailyPlan extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private Long day;

    public DailyPlan(Schedule checkSchedule, DailyPlanRequestDto dailyPlanRequestDto) {
        this.schedule = checkSchedule;
        this.day = dailyPlanRequestDto.getDay();
    }

}

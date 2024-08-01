package com.befriend.detour.domain.schedule.repository;

import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

    @Modifying
    @Query("UPDATE Schedule s SET s.hits = s.hits + 1 WHERE s.id = :scheduleId")
    int updateHits(@Param("scheduleId") Long scheduleId);

    @Modifying
    @Query("UPDATE Schedule s SET s.hourHits = s.hourHits + 1 WHERE s.id = :scheduleId")
    int updateHourHits(@Param("scheduleId") Long scheduleId);

}

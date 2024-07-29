package com.befriend.detour.domain.schedule.repository;

import com.befriend.detour.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepositoryCustom {

    Optional<List<Schedule>> findSchedulesByCreatedUser(Long userId, Pageable pageable);

}

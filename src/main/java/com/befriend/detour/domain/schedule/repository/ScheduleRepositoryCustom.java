package com.befriend.detour.domain.schedule.repository;

import com.befriend.detour.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepositoryCustom {

    Page<Schedule> findSchedulesByCreatedUser(Long userId, Pageable pageable);

    Page<Schedule> findSchedulesByCreatedUserBySearch(Long userId, Pageable pageable, String search);

    Optional<List<Long>> getScheduleIdRanking();

    void deleteAllHourHits();

    Page<Schedule> findAllBySearch(Pageable pageable, String search);

}

package com.befriend.detour.domain.like.repository;

import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepositoryCustom {

    boolean existsByUserAndSchedule(User user, Schedule schedule);

    Like findLikeWithSchedule(Long likeId);

    Page<Schedule> getUserLikedSchedules(User user, Pageable pageable);

    Page<Schedule> getUserLikedSchedulesBySearch(User user, Pageable pageable, String search);

}
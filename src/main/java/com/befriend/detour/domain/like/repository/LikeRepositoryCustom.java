package com.befriend.detour.domain.like.repository;

import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepositoryCustom {

    boolean existsByUserAndSchedule(User user, Schedule schedule);

}
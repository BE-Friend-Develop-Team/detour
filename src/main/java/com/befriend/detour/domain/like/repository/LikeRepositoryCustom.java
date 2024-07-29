package com.befriend.detour.domain.like.repository;

import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepositoryCustom {

    boolean existsByUserAndSchedule(User user, Schedule schedule);
    Like findLikeWithSchedule(Long likeId);

    Optional<List<Like>> getUserLikedSchedules(User user, Pageable pageable);

}
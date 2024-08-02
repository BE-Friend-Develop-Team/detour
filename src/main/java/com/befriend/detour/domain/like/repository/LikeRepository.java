package com.befriend.detour.domain.like.repository;

import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {

    @Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.schedule.id = :scheduleId")
    boolean existsByScheduleId(Long scheduleId);

    // 기존 메소드
    @Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.user = :user AND l.schedule = :schedule")
    boolean existsByUserAndSchedule(User user, Schedule schedule);

    Optional<Like> findByScheduleAndUser(Schedule schedule, User user);

    Optional<Like> findByUserIdAndScheduleId(Long userId, Long scheduleId);
}
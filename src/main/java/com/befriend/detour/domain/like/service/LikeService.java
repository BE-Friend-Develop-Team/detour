package com.befriend.detour.domain.like.service;

import com.befriend.detour.domain.like.dto.LikeResponseDto;
import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.like.repository.LikeRepository;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.repository.ScheduleRepository;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;
    private final ScheduleService scheduleService;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final RedissonClient redissonClient;

    private static final String LOCK_KEY_PREFIX = "like_lock_";

    @Transactional
    public LikeResponseDto createScheduleLike(Long scheduleId, User user) {
        Schedule foundSchedule = scheduleService.findById(scheduleId);

        if (likeRepository.existsByUserAndSchedule(user, foundSchedule)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        Like like = new Like(user, foundSchedule);
        likeRepository.save(like);
        foundSchedule.addLikeCount();

        return new LikeResponseDto(like, true);
    }

    public LikeResponseDto createScheduleLikeWithLock(Long scheduleId, User user) {
        String lockKey = LOCK_KEY_PREFIX + scheduleId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(20, 30, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }

            try {

                return createScheduleLikeTransactional(scheduleId, user);
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Lock acquisition interrupted", e);
        }
    }

    @Transactional
    public LikeResponseDto createScheduleLikeTransactional(Long scheduleId, User user) {
        Schedule foundSchedule = scheduleService.findById(scheduleId);

        if (likeRepository.existsByUserAndSchedule(user, foundSchedule)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        Like like = new Like(user, foundSchedule);
        likeRepository.save(like);

        foundSchedule.addLikeCount();
        scheduleRepository.saveAndFlush(foundSchedule);

        return new LikeResponseDto(like, true);
    }

    @Transactional
    public LikeResponseDto deleteScheduleLike(Long likeId, User user) {
        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + likeId);
        try {
            boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }

            try {
                Like foundLike = likeRepository.findLikeWithSchedule(likeId);

                if (foundLike == null) {
                    throw new CustomException(ErrorCode.LIKE_NOT_EXIST);
                }

                Schedule foundSchedule = foundLike.getSchedule();

                if (!user.getId().equals(foundLike.getUser().getId())) {
                    throw new CustomException(ErrorCode.CANNOT_CANCEL_OTHERS_LIKE);
                }

                likeRepository.delete(foundLike);
                foundSchedule.minusLikeCount();

                return new LikeResponseDto(null, false);
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Lock acquisition interrupted", e);
        }
    }

    public LikeResponseDto getLike(Long scheduleId, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        Like like = likeRepository.findByScheduleAndUser(schedule, user)
                .orElseThrow(() -> new CustomException(ErrorCode.ALREADY_INVITED));

        return new LikeResponseDto(like, false);
    }

}
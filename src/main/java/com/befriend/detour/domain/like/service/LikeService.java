package com.befriend.detour.domain.like.service;

import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.like.repository.LikeRepository;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import com.befriend.detour.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ScheduleService scheduleService;
    private final RedisService redisService;

    private static final String LIKE_LOCK_PREFIX = "like_lock:";

    public void createScheduleLike(Long scheduleId, User user) {
        String lockKey = LIKE_LOCK_PREFIX + scheduleId + ":" + user.getId();

        // 분산 잠금을 획득하려고 시도
        if (!redisService.acquireLock(lockKey)) {
            throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
        }

        try {
            createScheduleLikeInternal(scheduleId, user);
        } finally {
            // 잠금을 해제
            redisService.releaseLock(lockKey);
        }
    }

    @Transactional
    public void createScheduleLikeInternal(Long scheduleId, User user) {
        Schedule foundSchedule = scheduleService.findById(scheduleId);

        if (likeRepository.findLikeByUserAndSchedule(user, foundSchedule).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        Like like = new Like(user, foundSchedule);
        likeRepository.save(like);
        foundSchedule.addLikeCount();
    }

    public void deleteScheduleLike(Long likeId, User user) {
        String lockKey = LIKE_LOCK_PREFIX + likeId + ":" + user.getId();

        // 분산 잠금을 획득하려고 시도
        if (!redisService.acquireLock(lockKey)) {
            throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
        }

        try {
            deleteScheduleLikeInternal(likeId, user);
        } finally {
            // 잠금을 해제
            redisService.releaseLock(lockKey);
        }
    }

    @Transactional
    public void deleteScheduleLikeInternal(Long likeId, User user) {
        Like foundLike = likeRepository.findById(likeId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_EXIST));
        Schedule foundSchedule = scheduleService.findById(foundLike.getSchedule().getId());

        if (!user.getId().equals(foundLike.getUser().getId())) {
            throw new CustomException(ErrorCode.CANNOT_CANCEL_OTHERS_LIKE);
        }

        likeRepository.delete(foundLike);
        foundSchedule.minusLikeCount();
    }
}

package com.befriend.detour.domain.like.service;

import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.like.repository.LikeRepository;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ScheduleService scheduleService;

    @Transactional
    public void createScheduleLike(Long scheduleId, User user) {
        Schedule foundschedule = scheduleService.findById(scheduleId);

        if (likeRepository.findLikeByUserAndSchedule(user, foundschedule).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        Like like = new Like(user, foundschedule);
        likeRepository.save(like);
        foundschedule.addLikeCount();
    }

}
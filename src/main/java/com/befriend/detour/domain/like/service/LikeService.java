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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ScheduleService scheduleService;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

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

    @Transactional
    public LikeResponseDto deleteScheduleLike(Long likeId, User user) {
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
    }

    public LikeResponseDto getLike(Long scheduleId, User user) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() ->
                new CustomException(ErrorCode.PLACE_NOT_FOUND));

        System.out.println(schedule.getId());
        System.out.println(user.getId());

        Like like = likeRepository.findByScheduleAndUser(schedule, user).orElseThrow(() ->
                new CustomException(ErrorCode.ALREADY_INVITED));

        System.out.println(like);

        return new LikeResponseDto(like, false);
    }

}
package com.befriend.detour.domain.schedule.service;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.invitation.repository.InvitationRepository;
import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.like.repository.LikeRepository;
import com.befriend.detour.domain.schedule.dto.*;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.repository.ScheduleRepository;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    @Value("${DEFAULT_IMAGE_URL}")
    private String defaultImageUrl;

    private final ScheduleRepository scheduleRepository;
    private final InvitationRepository invitationRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto, User user) {
        Schedule schedule = new Schedule(scheduleRequestDto, user, defaultImageUrl);
        scheduleRepository.save(schedule);
        Invitation invitation = new Invitation(user, schedule);
        invitationRepository.save(invitation);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateScheduleTitle(Long scheduleId, EditTitleRequestDto editTitleRequestDto, User user) {
        Schedule checkSchedule = findById(scheduleId);
        checkIfMemberOfSchedule(checkSchedule, user);
        checkSchedule.updateScheduleTitle(editTitleRequestDto);
        scheduleRepository.save(checkSchedule);

        return new ScheduleResponseDto(checkSchedule);
    }

    @Transactional
    public ScheduleResponseDto updateScheduleDate(Long scheduleId, EditDateRequestDto editDateRequestDto, User user) {
        Schedule checkSchedule = findById(scheduleId);
        checkIfMemberOfSchedule(checkSchedule, user);
        checkSchedule.updateScheduleDate(editDateRequestDto);
        scheduleRepository.save(checkSchedule);

        return new ScheduleResponseDto(checkSchedule);
    }

    @Transactional
    public ScheduleResponseDto updateScheduleMainImage(Long scheduleId, EditMainImageRequestDto editMainImageRequestDto, User user) {
        Schedule checkSchedule = findById(scheduleId);
        checkIfMemberOfSchedule(checkSchedule, user);
        checkSchedule.updateScheduleMainImage(editMainImageRequestDto);
        scheduleRepository.save(checkSchedule);

        return new ScheduleResponseDto(checkSchedule);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, User user) {
        Schedule checkSchedule = findById(scheduleId);
        checkIfMemberOfSchedule(checkSchedule, user);
        scheduleRepository.delete(checkSchedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getUserCreatedSchedules(Pageable pageable, Long userId) {
        List<Schedule> schedules = scheduleRepository.findSchedulesByCreatedUser(userId, pageable).orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (schedules.isEmpty()) {
            throw new CustomException(ErrorCode.USER_CREATED_SCHEDULES_NOT_FOUND);
        }

        return schedules.stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getUserLikedSchedules(Pageable pageable, User user) {
        List<Like> likes = likeRepository.getUserLikedSchedules(user, pageable)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_EXIST));

        List<Schedule> schedules = likes.stream()
                .map(Like::getSchedule)
                .collect(Collectors.toList());

        return schedules.stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleResponseDto getSchedule(Long scheduleId) {
        Schedule schedule = findById(scheduleId);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getSchedules(String sortBy, int page, int size) {
        Sort sort;

        if (sortBy.equals("좋아요")) {
            sort = Sort.by(Sort.Order.desc("likeCount"));
        } else if (sortBy.equals("최신")) {
            sort = Sort.by(Sort.Order.desc("createdAt"));
        } else {
            throw new CustomException(ErrorCode.SORT_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        List<Schedule> schedules = scheduleRepository.findAll(pageable).getContent();

        return schedules.stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    public void checkIfMemberOfSchedule(Schedule schedule, User user) {
        if (invitationRepository.findInvitationByScheduleAndUser(schedule, user) == null) {
            throw new CustomException(ErrorCode.NOT_SCHEDULE_MEMBER);
        }
    }

    @Transactional(readOnly = true)
    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
    }

}
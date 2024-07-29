package com.befriend.detour.domain.schedule.service;

import com.befriend.detour.domain.invitation.service.InvitationService;
import com.befriend.detour.domain.schedule.dto.*;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.repository.ScheduleRepository;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    @Value("${DEFAULT_IMAGE_URL}")
    private String defaultImageUrl;

    private final ScheduleRepository scheduleRepository;
    private final InvitationService invitationService;

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto, User user) {
        Schedule schedule = new Schedule(scheduleRequestDto, user, defaultImageUrl);
        scheduleRepository.save(schedule);
        invitationService.createInvitation(user, schedule);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateScheduleTitle(Long scheduleId, EditTitleRequestDto editTitleRequestDto, User user) {
        Schedule checkSchedule = findById(scheduleId);
        invitationService.checkIfMemberOfSchedule(checkSchedule, user);
        checkSchedule.updateScheduleTitle(editTitleRequestDto);
        scheduleRepository.save(checkSchedule);

        return new ScheduleResponseDto(checkSchedule);
    }

    @Transactional
    public ScheduleResponseDto updateScheduleDate(Long scheduleId, EditDateRequestDto editDateRequestDto, User user) {
        Schedule checkSchedule = findById(scheduleId);
        invitationService.checkIfMemberOfSchedule(checkSchedule, user);
        checkSchedule.updateScheduleDate(editDateRequestDto);
        scheduleRepository.save(checkSchedule);

        return new ScheduleResponseDto(checkSchedule);
    }

    @Transactional
    public ScheduleResponseDto updateScheduleMainImage(Long scheduleId, EditMainImageRequestDto editMainImageRequestDto, User user) {
        Schedule checkSchedule = findById(scheduleId);
        invitationService.checkIfMemberOfSchedule(checkSchedule, user);
        checkSchedule.updateScheduleMainImage(editMainImageRequestDto);
        scheduleRepository.save(checkSchedule);

        return new ScheduleResponseDto(checkSchedule);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, User user) {
        Schedule checkSchedule = findById(scheduleId);
        invitationService.checkIfMemberOfSchedule(checkSchedule, user);
        scheduleRepository.delete(checkSchedule);
    }

    @Transactional(readOnly = true)
    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
    }

}
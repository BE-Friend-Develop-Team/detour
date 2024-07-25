package com.befriend.detour.domain.schedule.service;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.invitation.repository.InvitationRepository;
import com.befriend.detour.domain.schedule.dto.*;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.repository.ScheduleRepository;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.service.UserService;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final InvitationRepository invitationRepository;
    private final UserService userService;

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto, User user) {
        Schedule schedule = new Schedule(scheduleRequestDto, user);
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
    public void deleteSchedule(Long scheduleId, User user) {
        Schedule checkSchedule = findById(scheduleId);
        checkIfMemberOfSchedule(checkSchedule, user);
        scheduleRepository.delete(checkSchedule);
    }

    @Transactional
    public void inviteMember(Long scheduleId, InvitationRequestDto invitationRequestDto, User user) {
        Schedule checkSchedule = findById(scheduleId);

        // 초대자가 해당 Schedule에 대한 권한이 있는 사용자인지 확인
        checkIfMemberOfSchedule(checkSchedule, user);

        User invitee = userService.findUserByNickName(invitationRequestDto.getNickname());

        // 초대할 사람이 이미 초대 받은 사용자인지 확인
        if (invitationRepository.findInvitationByScheduleAndUser(checkSchedule, invitee) != null) {
            throw new CustomException(ErrorCode.ALREADY_INVITED);
        }

        Invitation invitation = new Invitation(invitee, checkSchedule);
        invitationRepository.save(invitation);
    }

    @Transactional
    public void cancelInvitation(Long scheduleId, InvitationRequestDto invitationRequestDto, User user) {
        Schedule checkSchedule = findById(scheduleId);

        // 초대취소자가 해당 Schedule에 대한 권한이 있는 사용자인지 확인
        checkIfMemberOfSchedule(checkSchedule, user);

        User invitee = userService.findUserByNickName(invitationRequestDto.getNickname());

        // 초대를 취소할 사람이 해당 일정의 일행인지 확인
        Invitation invitation = invitationRepository.findInvitationByScheduleAndUser(checkSchedule, invitee);
        if (invitation == null) {
            throw new CustomException(ErrorCode.USER_NOT_MEMBER);
        }

        invitationRepository.delete(invitation);
    }

    private void checkIfMemberOfSchedule(Schedule schedule, User user) {
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

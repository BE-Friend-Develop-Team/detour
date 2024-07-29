package com.befriend.detour.domain.invitation.service;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.invitation.repository.InvitationRepository;
import com.befriend.detour.domain.invitation.dto.InvitationRequestDto;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.service.UserService;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserService userService;
    private final ScheduleService scheduleService;

    @Transactional
    public void inviteMember(Long scheduleId, InvitationRequestDto invitationRequestDto, User user) {
        Schedule checkSchedule = scheduleService.findById(scheduleId);

        // 초대자가 해당 Schedule에 대한 권한이 있는 사용자인지 확인
        scheduleService.checkIfMemberOfSchedule(checkSchedule, user);

        User invitee = userService.findUserByNickName(invitationRequestDto.getNickname());

        // 초대할 사람이 이미 초대 받은 사용자인지 확인
        if (invitationRepository.existsByScheduleAndUser(checkSchedule, invitee)) {
            throw new CustomException(ErrorCode.ALREADY_INVITED);
        }

        Invitation invitation = new Invitation(invitee, checkSchedule);
        invitationRepository.save(invitation);
    }

    @Transactional
    public void cancelInvitation(Long scheduleId, InvitationRequestDto invitationRequestDto, User user) {
        Schedule checkSchedule = scheduleService.findById(scheduleId);

        // 초대취소자가 해당 Schedule에 대한 권한이 있는 사용자인지 확인
        scheduleService.checkIfMemberOfSchedule(checkSchedule, user);

        User invitee = userService.findUserByNickName(invitationRequestDto.getNickname());

        // 초대를 취소할 사람이 해당 일정의 일행인지 확인
        if (!invitationRepository.existsByScheduleAndUser(checkSchedule, invitee)) {
            throw new CustomException(ErrorCode.USER_NOT_MEMBER);
        }

        Invitation invitation = invitationRepository.findInvitationByScheduleAndUser(checkSchedule, invitee);
        invitationRepository.delete(invitation);
    }

}
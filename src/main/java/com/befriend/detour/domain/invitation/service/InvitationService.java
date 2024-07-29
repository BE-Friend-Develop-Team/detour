package com.befriend.detour.domain.invitation.service;

import com.befriend.detour.domain.invitation.dto.InvitationRequestDto;
import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.invitation.repository.InvitationRepository;
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
        Schedule schedule = getScheduleWithAuthorization(scheduleId, user);
        User invitee = userService.findUserByNickName(invitationRequestDto.getNickname());

        if (invitationRepository.existsByScheduleAndUser(schedule, invitee)) {
            throw new CustomException(ErrorCode.ALREADY_INVITED);
        }

        createInvitation(invitee, schedule);
    }

    @Transactional
    public void cancelInvitation(Long scheduleId, InvitationRequestDto invitationRequestDto, User user) {
        Schedule schedule = getScheduleWithAuthorization(scheduleId, user);
        User invitee = userService.findUserByNickName(invitationRequestDto.getNickname());

        // 초대를 취소할 사람이 해당 일정의 일행인지 확인
        invitationRepository.checkIfMemberOfSchedule(schedule, invitee);

        Invitation invitation = invitationRepository.findInvitationByScheduleAndUser(schedule, invitee)
                .orElseThrow(() -> new CustomException(ErrorCode.INVITATION_NOT_FOUND));
        invitationRepository.delete(invitation);
    }

    public void createInvitation(User user, Schedule schedule) {
        Invitation invitation = new Invitation(user, schedule);
        invitationRepository.save(invitation);
    }

    private Schedule getScheduleWithAuthorization(Long scheduleId, User user) {
        Schedule schedule = scheduleService.findById(scheduleId);
        invitationRepository.checkIfMemberOfSchedule(schedule, user);
        return schedule;
    }

}
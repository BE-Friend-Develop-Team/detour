package com.befriend.detour.domain.schedule.service;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.invitation.repository.InvitationRepository;
import com.befriend.detour.domain.schedule.dto.InvitationRequestDto;
import com.befriend.detour.domain.schedule.dto.ScheduleRequestDto;
import com.befriend.detour.domain.schedule.dto.ScheduleResponseDto;
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
    public void inviteMember(Long scheduleId, InvitationRequestDto invitationRequestDto, User user) {
        Schedule checkSchedule = findById(scheduleId);

        // 초대자가 해당 Schedule에 대한 권한이 있는 사용자인지 확인
        checkIfMemberOfSchedule(checkSchedule, user);

        User invitee = userService.findUserByNickName(invitationRequestDto.getNickname());

        // 초대할 사람이 이미 초대 받은 사용자인지 확인
        if (invitationRepository.isMemberOfSchedule(checkSchedule, invitee)) {
            throw new CustomException(ErrorCode.ALREADY_INVITED);
        }

        Invitation invitation = new Invitation(invitee, checkSchedule);
        invitationRepository.save(invitation);
    }

    private void checkIfMemberOfSchedule(Schedule schedule, User user) {
        boolean isMemberOfSchedule = invitationRepository.isMemberOfSchedule(schedule, user);
        if (!isMemberOfSchedule) {
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

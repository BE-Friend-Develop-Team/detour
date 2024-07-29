package com.befriend.detour.domain.schedule.service;

import com.befriend.detour.domain.invitation.service.InvitationService;
import com.befriend.detour.domain.schedule.dto.ScheduleRequestDto;
import com.befriend.detour.domain.schedule.dto.ScheduleResponseDto;
import com.befriend.detour.domain.schedule.dto.ScheduleUpdateRequestDto;
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
    public ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleUpdateRequestDto updateRequestDto, User user) {
        Schedule schedule = findById(scheduleId);
        invitationService.checkIfMemberOfSchedule(schedule, user);
        updateScheduleFields(schedule, updateRequestDto);
        scheduleRepository.save(schedule);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, User user) {
        Schedule checkSchedule = findById(scheduleId);
        invitationService.checkIfMemberOfSchedule(checkSchedule, user);
        scheduleRepository.delete(checkSchedule);
    }

    private void updateScheduleFields(Schedule schedule, ScheduleUpdateRequestDto updateRequestDto) {
        if (updateRequestDto.getTitle() != null) {
            schedule.updateScheduleTitle(updateRequestDto.getTitle());
        }
        if (updateRequestDto.getDepartureDate() != null) {
            schedule.updateDepartureDate(updateRequestDto.getDepartureDate());
        }
        if (updateRequestDto.getArrivalDate() != null) {
            schedule.updateArrivalDate(updateRequestDto.getArrivalDate());
        }
        if (updateRequestDto.getMainImage() != null) {
            schedule.updateScheduleMainImage(updateRequestDto.getMainImage());
        }
    }

    @Transactional(readOnly = true)
    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
    }

}
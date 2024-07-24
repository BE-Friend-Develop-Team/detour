package com.befriend.detour.domain.schedule.controller;

import com.befriend.detour.domain.schedule.dto.InvitationRequestDto;
import com.befriend.detour.domain.schedule.dto.ScheduleRequestDto;
import com.befriend.detour.domain.schedule.dto.ScheduleResponseDto;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<CommonResponseDto<ScheduleResponseDto>> createSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
                                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(scheduleRequestDto, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(201, "일정 생성에 성공하였습니다. 🎉", scheduleResponseDto), HttpStatus.CREATED);
    }

    @PostMapping("/{scheduleId}/invitation")
    public ResponseEntity<CommonResponseDto> inviteMember(@PathVariable(value = "scheduleId") Long scheduleId,
                                                           @Valid @RequestBody InvitationRequestDto invitationRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scheduleService.inviteMember(scheduleId, invitationRequestDto, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(201, "일행 초대에 성공하였습니다. 🎉", null), HttpStatus.CREATED);
    }

}

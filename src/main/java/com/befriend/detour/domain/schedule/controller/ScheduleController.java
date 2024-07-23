package com.befriend.detour.domain.schedule.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

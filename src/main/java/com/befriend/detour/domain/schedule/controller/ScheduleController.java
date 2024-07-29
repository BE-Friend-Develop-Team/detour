package com.befriend.detour.domain.schedule.controller;

import com.befriend.detour.domain.schedule.dto.*;
import com.befriend.detour.domain.schedule.service.ScheduleService;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/{scheduleId}/title")
    public ResponseEntity<CommonResponseDto<ScheduleResponseDto>> updateScheduleTitle(@PathVariable(value = "scheduleId") Long scheduleId,
                                                                                      @Valid @RequestBody EditTitleRequestDto editTitleRequestDto,
                                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.updateScheduleTitle(scheduleId, editTitleRequestDto, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(200, "일정 제목 수정에 성공하였습니다. 🎉", scheduleResponseDto));
    }

    @PatchMapping("/{scheduleId}/date")
    public ResponseEntity<CommonResponseDto<ScheduleResponseDto>> updateScheduleDate(@PathVariable(value = "scheduleId") Long scheduleId,
                                                                                     @Valid @RequestBody EditDateRequestDto editDateRequestDto,
                                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.updateScheduleDate(scheduleId, editDateRequestDto, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(200, "일정 기간 수정에 성공하였습니다. 🎉", scheduleResponseDto));
    }

    @PatchMapping("/{scheduleId}/main-image")
    public ResponseEntity<CommonResponseDto<ScheduleResponseDto>> updateScheduleMainImage(@PathVariable(value = "scheduleId") Long scheduleId,
                                                                                          @Valid @RequestBody EditMainImageRequestDto editMainImageRequestDto,
                                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.updateScheduleMainImage(scheduleId, editMainImageRequestDto, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(200, "일정 메인 이미지 수정에 성공하였습니다. 🎉", scheduleResponseDto));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<CommonResponseDto> deleteSchedule(@PathVariable(value = "scheduleId") Long scheduleId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scheduleService.deleteSchedule(scheduleId, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(200, "일정 삭제에 성공하였습니다. 🎉", null));
    }

    @GetMapping("/users")
    public ResponseEntity<CommonResponseDto> getUserCreatedSchedules(@RequestParam(value = "page") int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page - 1, 12);
        List<ScheduleResponseDto> scheduleResponseDtos = scheduleService.getUserCreatedSchedules(pageable, userDetails.getUser().getId());

        return ResponseEntity.ok(new CommonResponseDto<>(200, userDetails.getUser().getNickname() + " 사용자가 작성한 일정들 조회에 성공하였습니다. 🎉", scheduleResponseDtos));
    }

    @GetMapping("/users/likes")
    public ResponseEntity<CommonResponseDto> getUserLikedSchedules(@RequestParam(value = "page") int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page - 1, 12);
        List<ScheduleResponseDto> scheduleResponseDtos = scheduleService.getUserLikedSchedules(pageable, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(200, userDetails.getUser().getNickname() + " 사용자가 좋아요한 일정들 조회에 성공하였습니다. 🎉", scheduleResponseDtos));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<CommonResponseDto> getSchedule(@PathVariable(value = "scheduleId") Long scheduleId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.getSchedule(scheduleId);

        return ResponseEntity.ok(new CommonResponseDto<>(200, "일정 조회에 성공하였습니다. 🎉", scheduleResponseDto));
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto> getSchedules(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "sortBy") String sortBy,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ScheduleResponseDto> scheduleResponseDtos = scheduleService.getSchedules(sortBy, page - 1, 12);

        return ResponseEntity.ok(new CommonResponseDto<>(200, sortBy + " 순으로 전체 일정을 조회에 성공하였습니다.", scheduleResponseDtos));
    }

}

package com.befriend.detour.domain.schedule.controller;

import com.befriend.detour.domain.file.entity.File;
import com.befriend.detour.domain.file.service.FileService;
import com.befriend.detour.domain.invitation.repository.InvitationRepository;
import com.befriend.detour.domain.schedule.dao.RankingDao;
import com.befriend.detour.domain.schedule.dto.ScheduleDetailsResponseDto;
import com.befriend.detour.domain.schedule.dto.ScheduleRequestDto;
import com.befriend.detour.domain.schedule.dto.ScheduleResponseDto;
import com.befriend.detour.domain.schedule.dto.ScheduleUpdateRequestDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final FileService fileService;
    private final RankingDao rankingDao;

    @PostMapping
    public ResponseEntity<CommonResponseDto<ScheduleResponseDto>> createSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
                                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(scheduleRequestDto, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(HttpStatus.CREATED.value(), "일정 생성에 성공하였습니다. 🎉", scheduleResponseDto), HttpStatus.CREATED);
    }


    @PatchMapping("/{scheduleId}")
    public ResponseEntity<CommonResponseDto<ScheduleDetailsResponseDto>> updateSchedule(@PathVariable(value = "scheduleId") Long scheduleId,
                                                                                 @Valid @RequestBody ScheduleUpdateRequestDto updateRequestDto,
                                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleDetailsResponseDto scheduleResponseDto = scheduleService.updateSchedule(scheduleId, updateRequestDto, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK.value(), "일정 수정에 성공하였습니다. 🎉", scheduleResponseDto));
    }

    @PatchMapping("/{scheduleId}/files")
    public ResponseEntity<CommonResponseDto<ScheduleResponseDto>> updateScheduleImage(@PathVariable Long scheduleId,
                                                                                      @RequestParam("file") MultipartFile file,
                                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        File uploadedFile = fileService.uploadFile(Collections.singletonList(file), null).get(0);
        ScheduleResponseDto scheduleResponseDto = scheduleService.updateMainImage(scheduleId, uploadedFile.getFileUrl(), userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK.value(), "이미지 수정에 성공하였습니다. 🎉", scheduleResponseDto));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteSchedule(@PathVariable(value = "scheduleId") Long scheduleId,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scheduleService.deleteSchedule(scheduleId, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK.value(), "일정 삭제에 성공하였습니다. 🎉", null));
    }

    @GetMapping("/users")
    public ResponseEntity<CommonResponseDto<List<ScheduleResponseDto>>> getUserCreatedSchedules(@RequestParam(value = "page") int page,
                                                                                                @RequestParam(value = "search", required = false) String search,
                                                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page - 1, 12);

        List<ScheduleResponseDto> scheduleResponseDtos = scheduleService.getUserCreatedSchedules(pageable, userDetails.getUser().getId(), search);

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK.value(), userDetails.getUser().getNickname() + " 사용자가 작성한 일정들 조회에 성공하였습니다. 🎉", scheduleResponseDtos));
    }

    @GetMapping("/users/likes")
    public ResponseEntity<CommonResponseDto<List<ScheduleResponseDto>>> getUserLikedSchedules(@RequestParam(value = "page") int page,
                                                                                              @RequestParam(value = "search", required = false) String search,
                                                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page - 1, 12);
        List<ScheduleResponseDto> scheduleResponseDtos = scheduleService.getUserLikedSchedules(pageable, userDetails.getUser(), search);

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK.value(), userDetails.getUser().getNickname() + " 사용자가 좋아요한 일정들 조회에 성공하였습니다. 🎉", scheduleResponseDtos));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<CommonResponseDto<ScheduleResponseDto>> getSchedule(
            @PathVariable("scheduleId") Long scheduleId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ScheduleResponseDto scheduleResponseDto = scheduleService.getSchedule(scheduleId, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK.value(), "일정 조회에 성공하였습니다. 🎉", scheduleResponseDto));
    }

    @GetMapping("/{scheduleId}/details")
    public ResponseEntity<CommonResponseDto<ScheduleDetailsResponseDto>> getScheduleDetails(
            @PathVariable("scheduleId") Long scheduleId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ScheduleDetailsResponseDto scheduleDetailsResponseDto = scheduleService.getScheduleDetails(scheduleId, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK.value(), "일정 조회에 성공하였습니다. 🎉", scheduleDetailsResponseDto));
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto<List<ScheduleResponseDto>>> getSchedules(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "sortBy") String sortBy,
            @RequestParam(value = "search", required = false) String search,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ScheduleResponseDto> scheduleResponseDtos = scheduleService.getSchedules(sortBy, page - 1, 12, search, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK.value(), sortBy + " 순으로 전체 일정을 조회에 성공하였습니다.", scheduleResponseDtos));
    }

    @GetMapping("/ranking")
    public ResponseEntity<CommonResponseDto<List<Long>>> getTopSchedules() {
        List<Long> ranking = rankingDao.getRanking();

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK.value(), "상위 12개의 인기 일정 ID를 성공적으로 조회했습니다.", ranking));
    }

}

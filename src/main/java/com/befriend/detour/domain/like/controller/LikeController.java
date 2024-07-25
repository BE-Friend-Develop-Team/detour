package com.befriend.detour.domain.like.controller;

import com.befriend.detour.domain.like.service.LikeService;
import com.befriend.detour.domain.schedule.dto.ScheduleResponseDto;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{scheduleId}/likes")
    public ResponseEntity<CommonResponseDto<ScheduleResponseDto>> createScheduleLike(@PathVariable(value = "scheduleId") Long scheduleId,
                                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.createScheduleLike(scheduleId, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(201, scheduleId + "번 일정에 대한 좋아요 등록을 성공하였습니다. 🎉", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/likes/{likeId}")
    public ResponseEntity<CommonResponseDto<ScheduleResponseDto>> deleteScheduleLike(@PathVariable(value = "likeId") Long likeId,
                                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.deleteScheduleLike(likeId, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(200, likeId + "번 좋아요 취소에 성공하였습니다. 🎉", null), HttpStatus.OK);
    }

}
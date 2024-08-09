package com.befriend.detour.domain.invitation.controller;

import com.befriend.detour.domain.invitation.dto.InvitationRequestDto;
import com.befriend.detour.domain.invitation.service.InvitationService;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/{scheduleId}/invitation")
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<CommonResponseDto> inviteMember(@PathVariable(value = "scheduleId") Long scheduleId,
                                                          @Valid @RequestBody InvitationRequestDto invitationRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        invitationService.inviteMember(scheduleId, invitationRequestDto, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseDto<>(201, "일행 초대에 성공하였습니다. 🎉", null), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<CommonResponseDto> cancelInvitation(@PathVariable(value = "scheduleId") Long scheduleId,
                                                              @Valid @RequestBody InvitationRequestDto invitationRequestDto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        invitationService.cancelInvitation(scheduleId, invitationRequestDto, userDetails.getUser());

        return ResponseEntity.ok(new CommonResponseDto<>(200, "초대 취소에 성공하였습니다. 🎉", null));
    }

    @GetMapping("/users")
    public ResponseEntity<CommonResponseDto> getUsers(@PathVariable(value = "scheduleId") Long scheduleId) {

        List<String> nicknames = invitationService.getUserNicknames(scheduleId);

        return ResponseEntity.ok(new CommonResponseDto<>(200, "전체 사용자 조회 성공하였습니다. 🎉", nicknames));
    }

}
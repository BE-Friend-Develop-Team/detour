package com.befriend.detour.domain.marker.controller;

import com.befriend.detour.domain.marker.dto.MarkerContentRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.service.MarkerService;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dailyPlans")
public class MarkerController {

    private final MarkerService markerService;

    // 마커 생성
    @PostMapping("/{dailPlanId}/markers/{placeId}")
    public ResponseEntity<CommonResponseDto> createMarker(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @PathVariable Long dailPlanId,
                                                          @PathVariable Long placeId,
                                                          MarkerRequestDto requestDto) {
        MarkerResponseDto responseDto = markerService.createMarker(userDetails.getUser().getNickname(), dailPlanId, placeId, requestDto);
        return ResponseEntity.ok(new CommonResponseDto(201, "마커 생성에 성공하였습니다. \uD83C\uDF89", responseDto));
    }

    // 마커 글 생성
    @PostMapping("/markers/{markerId}/content")
    public ResponseEntity<CommonResponseDto> createMarkerContent (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable Long markerId,
                                                                  MarkerContentRequestDto requestDto) {
        MarkerResponseDto responseDto = markerService.createMarkerContent(userDetails.getUser().getNickname(), markerId, requestDto);
        return ResponseEntity.ok(new CommonResponseDto(200, "마커 내 글 저장에 성공하였습니다. \uD83C\uDF89", responseDto));
    }

}

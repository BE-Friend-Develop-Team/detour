package com.befriend.detour.domain.marker.controller;

import com.befriend.detour.domain.marker.dto.MarkerContentRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.entity.Marker;
import com.befriend.detour.domain.marker.service.MarkerService;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        MarkerResponseDto responseDto = markerService.updateMarkerContent(userDetails.getUser().getNickname(), markerId, requestDto);
        return ResponseEntity.ok(new CommonResponseDto(200, "마커 내 글 저장에 성공하였습니다. \uD83C\uDF89", responseDto));
    }

    // 마커 글 수정
    @PostMapping("/markers/{markerId}/content")
    public ResponseEntity<CommonResponseDto> updateMarkerContent (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable Long markerId,
                                                                  MarkerContentRequestDto requestDto) {
        MarkerResponseDto responseDto = markerService.updateMarkerContent(userDetails.getUser().getNickname(), markerId, requestDto);
        return ResponseEntity.ok(new CommonResponseDto(200, "마커 내 글 수정에 성공하였습니다. \uD83C\uDF89", responseDto));
    }


    // 특정 데일리 플랜 마커 전체 조회
    @GetMapping("/{dailyPlanId}/marker")
    public ResponseEntity<CommonResponseDto> getAllMarker( @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long dailyPlanId) {

        List<MarkerResponseDto> responseDto = markerService.getAllMarker(userDetails.getUser().getNickname(), dailyPlanId);
        return ResponseEntity.ok(new CommonResponseDto(200, dailyPlanId + "번 데일리 플랜의 마커 전체 조회에 성공하였습니다. 🎉", responseDto));

    }

    // 마커 단건 조회
    @GetMapping("{dailyPlanId}/markers/{markerId}")
    public ResponseEntity<CommonResponseDto> getMarker(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable Long dailyPlanId,
                                                       @PathVariable Long markerId) {
        MarkerResponseDto responseDto = markerService.getMarker(userDetails.getUser().getNickname(), dailyPlanId, markerId);
        return ResponseEntity.ok(new CommonResponseDto(200, "마커 조회에 성공하였습니다. 🎉", responseDto));

    }

}

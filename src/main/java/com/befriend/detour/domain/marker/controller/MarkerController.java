package com.befriend.detour.domain.marker.controller;

import com.befriend.detour.domain.marker.dto.MarkerContentRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerLocationResponseDto;
import com.befriend.detour.domain.marker.dto.MarkerRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.service.MarkerFileService;
import com.befriend.detour.domain.marker.service.MarkerService;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/daily-plans")
public class MarkerController {

    private final MarkerService markerService;
    private final MarkerFileService markerFileService;

    // 마커 생성
    @PostMapping("/{dailyPlanId}/place/{placeId}/markers")
    public ResponseEntity<CommonResponseDto> createMarker(@PathVariable Long dailyPlanId,
                                                          @PathVariable Long placeId,
                                                          @Valid @RequestBody MarkerRequestDto requestDto) {
        MarkerResponseDto responseDto = markerService.createMarker(dailyPlanId, placeId, requestDto);

        return new ResponseEntity<>(new CommonResponseDto<>(201, "마커 생성에 성공하였습니다. 🎉", responseDto), HttpStatus.CREATED);
    }

    // 마커 글 생성
    @PostMapping("/markers/{markerId}/content")
    public ResponseEntity<CommonResponseDto> createMarkerContent(@PathVariable Long markerId,
                                                                 @Valid @RequestBody MarkerContentRequestDto requestDto) {
        MarkerResponseDto responseDto = markerService.updateMarkerContent(markerId, requestDto);

        return new ResponseEntity<>(new CommonResponseDto<>(201, "마커 내 글 저장에 성공하였습니다. 🎉", responseDto), HttpStatus.CREATED);
    }

    // 마커 글 수정
    @PatchMapping("/markers/{markerId}/content")
    public ResponseEntity<CommonResponseDto> updateMarkerContent(@PathVariable Long markerId,
                                                                 @Valid @RequestBody MarkerContentRequestDto requestDto) {
        MarkerResponseDto responseDto = markerService.updateMarkerContent(markerId, requestDto);

        return ResponseEntity.ok(new CommonResponseDto(200, "마커 내 글 수정에 성공하였습니다. 🎉", responseDto));
    }

    // 특정 데일리 플랜 마커 전체 조회
    @GetMapping("/{dailyPlanId}/markers")
    public ResponseEntity<CommonResponseDto> getAllMarker(@PathVariable Long dailyPlanId) {

        List<MarkerResponseDto> responseDto = markerService.getAllMarker(dailyPlanId);

        return ResponseEntity.ok(new CommonResponseDto(200, dailyPlanId + "번 데일리 플랜의 마커 전체 조회에 성공하였습니다. 🎉", responseDto));

    }

    // 마커 단건 조회
    @GetMapping("/{dailyPlanId}/markers/{markerId}")
    public ResponseEntity<CommonResponseDto> getMarker(@PathVariable Long dailyPlanId,
                                                       @PathVariable Long markerId) {
        MarkerResponseDto responseDto = markerService.getMarker(dailyPlanId, markerId);

        return ResponseEntity.ok(new CommonResponseDto(200, "마커 조회에 성공하였습니다. 🎉", responseDto));
    }

    // 위도 경도 조회
    @GetMapping("/markers/{markerId}/location")
    public ResponseEntity<CommonResponseDto> getPosition(@PathVariable Long markerId) {

        MarkerLocationResponseDto responseDto = markerService.getPosition(markerId);

        return ResponseEntity.ok(new CommonResponseDto(200, "위도, 경도 조회에 성공하였습니다. 🎉", responseDto));
    }

    // 마커 삭제
    @DeleteMapping("/markers/{markerId}")
    public ResponseEntity<CommonResponseDto> deleteMarker(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @PathVariable Long markerId) {
        markerService.deleteMarker(userDetails.getUser(), markerId);

        return ResponseEntity.ok(new CommonResponseDto(200, "마커 삭제에 성공하였습니다. 🎉", null));
    }

    // 파일 저장
    @PostMapping("/markers/{markerId}/files")
    public ResponseEntity<CommonResponseDto<List<MarkerResponseDto>>> uploadFile(@PathVariable Long markerId,
                                                                                 @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        List<MarkerResponseDto> responseDto = markerFileService.uploadFiles(markerId, multipartFiles);

        return new ResponseEntity<>(new CommonResponseDto<>(201, "파일 업로드에 성공하였습니다. 🎉", responseDto), HttpStatus.CREATED);
    }

    // 파일 삭제
    @DeleteMapping("/markers/{markerId}/files")
    public ResponseEntity<CommonResponseDto<String>> deleteFile(@PathVariable Long markerId,
                                                                @RequestParam String fileUrl) {
        markerFileService.deleteFile(markerId, fileUrl);

        return ResponseEntity.ok(new CommonResponseDto<>(200, "파일 삭제에 성공하였습니다. 🎉", null));
    }

}

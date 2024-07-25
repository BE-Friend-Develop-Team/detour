package com.befriend.detour.domain.place.controller;

import com.befriend.detour.domain.place.dto.PlaceRequestDto;
import com.befriend.detour.domain.place.dto.PlaceResponseDto;
import com.befriend.detour.domain.place.service.PlaceService;
import com.befriend.detour.global.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("/place")
    public ResponseEntity<CommonResponseDto<PlaceResponseDto>> createPlace(@RequestBody PlaceRequestDto placeRequestDto) {
        PlaceResponseDto responseDto = placeService.createPlace(placeRequestDto);

        return new ResponseEntity<>(new CommonResponseDto<>(201, "장소 저장에 성공하였습니다. 🎉", responseDto), HttpStatus.CREATED);
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<CommonResponseDto<PlaceResponseDto>> getPlace(@PathVariable Long placeId) {
        PlaceResponseDto responseDto = placeService.getPlace(placeId);

        return new ResponseEntity<>(new CommonResponseDto<>(200, "장소 조회에 성공하였습니다. 🎉", responseDto), HttpStatus.OK);
    }
}

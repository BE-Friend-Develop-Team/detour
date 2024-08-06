package com.befriend.detour.domain.review.controller;

import com.befriend.detour.domain.review.dto.ReviewRequestDto;
import com.befriend.detour.domain.review.dto.ReviewResponseDto;
import com.befriend.detour.domain.review.service.ReviewService;
import com.befriend.detour.global.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<CommonResponseDto<ReviewResponseDto>> createReview(@RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto responseDto = reviewService.createReview(requestDto);

        return new ResponseEntity<>(new CommonResponseDto<>(201, "리뷰 저장에 성공하였습니다. 🎉", null), HttpStatus.CREATED);
    }

}

package com.befriend.detour.domain.review.controller;

import com.befriend.detour.domain.review.dto.ReviewRequestDto;
import com.befriend.detour.domain.review.dto.ReviewResponseDto;
import com.befriend.detour.domain.review.service.ReviewService;
import com.befriend.detour.global.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<CommonResponseDto<ReviewResponseDto>> createReview(@RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto responseDto = reviewService.createReview(requestDto);

        return new ResponseEntity<>(new CommonResponseDto<>(201, "리뷰 저장에 성공하였습니다. 🎉", responseDto), HttpStatus.CREATED);
    }

    @GetMapping("/average")
    public ResponseEntity<CommonResponseDto<Double>> getAverageRating() {
        double averageRating = reviewService.getAverageRating();

        return new ResponseEntity<>(new CommonResponseDto<>(200, "리뷰 평균 점수입니다. 🎉", averageRating), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto<Page<ReviewResponseDto>>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponseDto> reviews = reviewService.getAllReviews(pageable);

        return new ResponseEntity<>(new CommonResponseDto<>(200, "리뷰 조회에 성공했습니다. 🎉", reviews), HttpStatus.OK);
    }

}

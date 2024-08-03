package com.befriend.detour.domain.review.service;

import com.befriend.detour.domain.review.dto.ReviewRequestDto;
import com.befriend.detour.domain.review.dto.ReviewResponseDto;
import com.befriend.detour.domain.review.entity.Review;
import com.befriend.detour.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewResponseDto createReview(ReviewRequestDto requestDto) {
        Review review = new Review(requestDto);
        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

}

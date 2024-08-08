package com.befriend.detour.domain.review.service;

import com.befriend.detour.domain.review.dto.ReviewRequestDto;
import com.befriend.detour.domain.review.dto.ReviewResponseDto;
import com.befriend.detour.domain.review.entity.Review;
import com.befriend.detour.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewResponseDto createReview(ReviewRequestDto requestDto) {
        Review review = new Review(requestDto);
        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    public double getAverageRating() {
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream()
                .mapToInt(Review::getStar)
                .average()
                .orElse(0.0);
    }

    public Page<ReviewResponseDto> getAllReviews(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));

        return reviewRepository.findAll(sortedPageable)
                .map(ReviewResponseDto::new);
    }
}
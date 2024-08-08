package com.befriend.detour.domain.review.dto;

import com.befriend.detour.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReviewResponseDto {

    private Long reviewId;
    private String content;
    private int star;
    private String username;
    private LocalDate createdDate;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.star = review.getStar();
        this.username = review.getUsername();
        this.createdDate = review.getCreatedAt().toLocalDate();

    }

}

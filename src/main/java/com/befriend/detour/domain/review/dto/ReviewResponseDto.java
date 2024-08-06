package com.befriend.detour.domain.review.dto;

import com.befriend.detour.domain.review.entity.Review;

public class ReviewResponseDto {

    private Long reciewId;
    private String content;
    private String star;

    public ReviewResponseDto(Review review) {
        this.reciewId = review.getId();
        this.content = review.getContent();
        this.star = review.getStar();
    }

}

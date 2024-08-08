package com.befriend.detour.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {

    private String content;
    private int star;
    private String username;
}

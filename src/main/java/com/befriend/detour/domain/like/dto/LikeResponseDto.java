package com.befriend.detour.domain.like.dto;

import com.befriend.detour.domain.like.entity.Like;
import lombok.Getter;

@Getter
public class LikeResponseDto {
    private Long likeId;
    private Long scheduleId;

    public LikeResponseDto(Like like) {
        this.likeId = like.getId();
        this.scheduleId = like.getSchedule().getId();
    }
}
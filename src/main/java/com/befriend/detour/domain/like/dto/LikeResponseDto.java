package com.befriend.detour.domain.like.dto;

import com.befriend.detour.domain.like.entity.Like;
import lombok.Getter;

@Getter
public class LikeResponseDto {

    private Long likeId;
    private Long scheduleId;
    private boolean isLiked;

    public LikeResponseDto(Like like, boolean isLiked) {
        this.likeId = (like != null) ? like.getId() : null;
        this.scheduleId = (like != null && like.getSchedule() != null) ? like.getSchedule().getId() : null;
        this.isLiked = isLiked;
    }

    public LikeResponseDto(Long likeId, Long scheduleId, boolean isLiked) {
        this.likeId = likeId;
        this.scheduleId = scheduleId;
        this.isLiked = isLiked;
    }

}
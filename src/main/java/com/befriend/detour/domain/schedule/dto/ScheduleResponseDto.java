package com.befriend.detour.domain.schedule.dto;

import com.befriend.detour.domain.like.dto.LikeResponseDto;
import com.befriend.detour.domain.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private Long scheduleId;
    private String title;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String imageUrl;
    private String nickname;
    private Long likeCount;
    private Long hits;
    private Long hourHits;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isLiked;

    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.title = schedule.getTitle();
        this.departureDate = schedule.getDepartureDate();
        this.arrivalDate = schedule.getArrivalDate();
        this.imageUrl = schedule.getImageUrl();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
        this.likeCount = schedule.getLikeCount();
        this.hits = schedule.getHits();
        this.hourHits = schedule.getHourHits();
        this.nickname = schedule.getUser().getNickname();
        this.isLiked = false;
    }

    public ScheduleResponseDto(Schedule schedule, LikeResponseDto likeResponseDto) {
        this.scheduleId = schedule.getId();
        this.title = schedule.getTitle();
        this.departureDate = schedule.getDepartureDate();
        this.arrivalDate = schedule.getArrivalDate();
        this.imageUrl = schedule.getImageUrl();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
        this.likeCount = schedule.getLikeCount();
        this.hits = schedule.getHits();
        this.hourHits = schedule.getHourHits();
        this.nickname = schedule.getUser().getNickname();
        this.isLiked = likeResponseDto.isLiked();
    }

}
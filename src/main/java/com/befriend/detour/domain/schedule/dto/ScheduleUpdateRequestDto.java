package com.befriend.detour.domain.schedule.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleUpdateRequestDto {

    private String title;
    private String imageUrl;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;

}
package com.befriend.detour.domain.schedule.dto;

import com.befriend.detour.domain.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class ScheduleResponseDto {

    private Long scheduleId;
    private String title;
    private Date departureDate;
    private Date arrivalDate;
//    private String mainImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.title = schedule.getTitle();
        this.departureDate = schedule.getDepartureDate();
        this.arrivalDate = schedule.getArrivalDate();
//        this.mainImage = Image.getFilePath();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }

}
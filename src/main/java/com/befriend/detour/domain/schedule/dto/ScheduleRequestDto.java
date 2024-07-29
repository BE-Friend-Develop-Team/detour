package com.befriend.detour.domain.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {

    @NotBlank(message = "일정의 제목을 입력해주세요.")
    private String title;

    @NotNull(message = "일정 시작 일자를 지정해주세요.")
    private LocalDateTime departureDate;

    @NotNull(message = "일정 종료 일자를 지정해주세요.")
    private LocalDateTime arrivalDate;

}
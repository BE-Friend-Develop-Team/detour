package com.befriend.detour.domain.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EditDateRequestDto {

    @NotNull(message = "일정 시작 일자를 지정해주세요.")
    private LocalDateTime departureDate;

    @NotNull(message = "일정 종료 일자를 지정해주세요.")
    private LocalDateTime arrivalDate;

}
package com.befriend.detour.domain.marker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MarkerRequestDto {

    @NotNull(message = "위도는 null일 수 없습니다.")
    private Double latitude;

    @NotNull(message = "경도는 null일 수 없습니다.")
    private Double longitude;

}
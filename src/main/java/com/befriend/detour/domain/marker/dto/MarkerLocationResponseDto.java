package com.befriend.detour.domain.marker.dto;

import lombok.Getter;

@Getter
public class MarkerLocationResponseDto {

    private Double latitude;
    private Double longitude;

    public MarkerLocationResponseDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}

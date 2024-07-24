package com.befriend.detour.domain.marker.dto;

import com.befriend.detour.domain.marker.entity.Marker;

public class MarkerResponseDto {

    private Double latitude;
    private Double longitude;
    private String content;
    private String images;

    public MarkerResponseDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MarkerResponseDto(Marker marker) {
        this.latitude = marker.getLatitude();
        this.longitude = marker.getLongitude();
        this.content = marker.getContent();
        this.images = marker.getImages();
    }
}

package com.befriend.detour.domain.marker.dto;

import com.befriend.detour.domain.marker.entity.Marker;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MarkerResponseDto {

    private Double latitude;
    private Double longitude;
    private String content;
    private List<String> images;

    public MarkerResponseDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MarkerResponseDto(Marker marker) {
        this.latitude = marker.getLatitude();
        this.longitude = marker.getLongitude();
        this.content = marker.getContent();
        // images는 나중에 설정될 수 있도록 null 또는 빈 리스트로 초기화 가능
        this.images = List.of();
    }

    public MarkerResponseDto(Marker marker, List<String> imageUrls) {
        this.latitude = marker.getLatitude();
        this.longitude = marker.getLongitude();
        this.content = marker.getContent();
        this.images = imageUrls;
    }

    public MarkerResponseDto(Double latitude, Double longitude, String content, List<String> images) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.content = content;
        this.images = images;
    }

}

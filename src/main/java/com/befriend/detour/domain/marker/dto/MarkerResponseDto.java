package com.befriend.detour.domain.marker.dto;

import com.befriend.detour.domain.marker.entity.Marker;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MarkerResponseDto {

    private Long markerId;
    private Long placeId;
    private Double latitude;
    private Double longitude;
    private String content;
    private List<String> images;
    private Long markerIndex;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String name;
    private String address;
    private String telNumber;

    public MarkerResponseDto(Marker marker) {
        this.markerId = marker.getId();
        this.placeId = marker.getPlace() != null ? marker.getPlace().getId() : null;
        this.latitude = marker.getLatitude();
        this.longitude = marker.getLongitude();
        this.content = marker.getContent();
        this.images = List.of();
        this.markerIndex = marker.getMarkerIndex();
        this.createdAt = marker.getCreatedAt();
        this.modifiedAt = marker.getModifiedAt();
        this.name = marker.getPlace().getName();
        this.address = marker.getPlace().getAddress();
        this.telNumber = marker.getPlace().getTelNumber();
    }

    public MarkerResponseDto(Marker marker, List<String> imageUrls) {
        this.markerId = marker.getId();
        this.placeId = marker.getPlace() != null ? marker.getPlace().getId() : null;
        this.latitude = marker.getLatitude();
        this.longitude = marker.getLongitude();
        this.content = marker.getContent();
        this.images = imageUrls;
        this.markerIndex = marker.getMarkerIndex();
        this.name = marker.getPlace().getName();
        this.createdAt = marker.getCreatedAt();
        this.modifiedAt = marker.getModifiedAt();
    }

    public MarkerResponseDto(Long placeId, Double latitude, Double longitude, String content, List<String> images, Long markerIndex, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.content = content;
        this.images = images;
        this.markerIndex = markerIndex;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}
package com.befriend.detour.domain.marker.dto;

import com.befriend.detour.domain.marker.entity.Marker;
import jakarta.persistence.Column;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MarkerResponseDto {

    private Long placeId;
    private Double latitude;
    private Double longitude;
    private String content;
    private List<String> images;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String name;
    private String address;
    private String telNumber;

    public MarkerResponseDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MarkerResponseDto(Marker marker) {
        this.placeId = marker.getPlace() != null ? marker.getPlace().getId() : null;
        this.latitude = marker.getLatitude();
        this.longitude = marker.getLongitude();
        this.content = marker.getContent();
        // images는 나중에 설정될 수 있도록 null 또는 빈 리스트로 초기화 가능
        this.images = List.of();
        this.createdAt = marker.getCreatedAt();  // 생성 시간 설정
        this.modifiedAt = marker.getModifiedAt(); // 수정 시간 설정
        this.name = marker.getPlace().getName();
        this.address = marker.getPlace().getAddress();
        this.telNumber = marker.getPlace().getTelNumber();
    }

    public MarkerResponseDto(Marker marker, List<String> imageUrls) {
        this.placeId = marker.getPlace() != null ? marker.getPlace().getId() : null;
        this.latitude = marker.getLatitude();
        this.longitude = marker.getLongitude();
        this.content = marker.getContent();
        this.images = imageUrls;
        this.createdAt = marker.getCreatedAt();  // 생성 시간 설정
        this.modifiedAt = marker.getModifiedAt(); // 수정 시간 설정
    }

    public MarkerResponseDto(Long placeId, Double latitude, Double longitude, String content, List<String> images, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.content = content;
        this.images = images;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}
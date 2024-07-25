package com.befriend.detour.domain.place.dto;

import com.befriend.detour.domain.place.entity.Place;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PlaceResponseDto {

    private Long placeId;
    private String name;
    private String address;
    private String telNumber;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PlaceResponseDto(Place place) {
        this.placeId = place.getId();
        this.name = place.getName();
        this.address = place.getAddress();
        this.telNumber = place.getTelNumber();
        this.createdAt = place.getCreatedAt();
        this.modifiedAt = place.getModifiedAt();
    }

}

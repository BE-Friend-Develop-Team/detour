package com.befriend.detour.domain.marker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MarkerMoveResponseDto {

    private Long markerId;
    private Long markerIndex;

    public MarkerMoveResponseDto(Long markerId, long markerIndex) {
        this.markerId = markerId;
        this.markerIndex = markerIndex;
    }
}
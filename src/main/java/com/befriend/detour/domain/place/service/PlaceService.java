package com.befriend.detour.domain.place.service;

import com.befriend.detour.domain.place.dto.PlaceRequestDto;
import com.befriend.detour.domain.place.dto.PlaceResponseDto;
import com.befriend.detour.domain.place.entity.Place;
import com.befriend.detour.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    // 장소 생성
    public PlaceResponseDto createPlace(PlaceRequestDto placeRequestDto) {
        Place place = new Place(placeRequestDto);
        placeRepository.save(place);

        return new PlaceResponseDto(place);
    }

}

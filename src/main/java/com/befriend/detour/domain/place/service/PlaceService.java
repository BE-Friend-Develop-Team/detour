package com.befriend.detour.domain.place.service;

import com.befriend.detour.domain.place.dto.PlaceRequestDto;
import com.befriend.detour.domain.place.dto.PlaceResponseDto;
import com.befriend.detour.domain.place.entity.Place;
import com.befriend.detour.domain.place.repository.PlaceRepository;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceResponseDto createPlace(PlaceRequestDto placeRequestDto) {
        Place place = new Place(placeRequestDto);
        placeRepository.save(place);

        return new PlaceResponseDto(place);
    }

    public PlaceResponseDto getPlace(Long placeId) {
        Place place = findPlaceById(placeId);

        return new PlaceResponseDto(place);
    }

    @Transactional
    public PlaceResponseDto updatePlace(Long placeId, PlaceRequestDto placeRequestDto) {
        Place place = findPlaceById(placeId);
        place.update(placeRequestDto);
        placeRepository.save(place);

        return new PlaceResponseDto(place);
    }

    @Transactional
    public void deletePlace(Long placeId) {
        Place place = findPlaceById(placeId);
        placeRepository.delete(place);
    }

    public Place findPlaceById(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new CustomException(ErrorCode.PLACE_NOT_FOUND)
        );

        return place;
    }

}

package com.befriend.detour.domain.marker.service;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.dailyplan.service.DailyPlanService;
import com.befriend.detour.domain.marker.dto.*;
import com.befriend.detour.domain.marker.entity.Marker;
import com.befriend.detour.domain.marker.entity.MarkerStatusEnum;
import com.befriend.detour.domain.marker.repository.MarkerRepository;
import com.befriend.detour.domain.place.entity.Place;
import com.befriend.detour.domain.place.service.PlaceService;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.service.UserService;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final DailyPlanService dailyPlanService;
    private final PlaceService placeService;
    private final UserService userService;

    @Transactional
    public MarkerResponseDto createMarker(Long dailyPlanId, Long placeId, MarkerRequestDto requestDto) {
        DailyPlan dailyPlan = dailyPlanService.findDailyPlanById(dailyPlanId);
        Place place = placeService.findPlaceById(placeId);

        Long maxIndex = markerRepository.findMaxMarkerIndexByDailyPlan(dailyPlan);
        maxIndex = (maxIndex == null) ? 0L : maxIndex + 1;

        Marker marker = new Marker(requestDto.getLatitude(), requestDto.getLongitude(), dailyPlan, place, maxIndex);
        markerRepository.save(marker);

        return new MarkerResponseDto(marker);
    }

    public List<MarkerResponseDto> getAllMarker(Long dailyPlanId) {
        dailyPlanService.findDailyPlanById(dailyPlanId);

        if (!isMarkerExist(dailyPlanId)) {
            throw new CustomException(ErrorCode.MARKER_NOT_FOUND_IN_DAILY_PLAN);
        }

        return markerRepository.findByDailyPlanId(dailyPlanId);
    }

    public MarkerResponseDto getMarker(Long dailyPlanId, Long markerId) {
        dailyPlanService.findDailyPlanById(dailyPlanId);

        if (!isMarkerExist(dailyPlanId)) {
            throw new CustomException(ErrorCode.MARKER_NOT_FOUND_IN_DAILY_PLAN);
        }

        return markerRepository.fetchMarkerDetails(markerId, dailyPlanId).orElseThrow(
                () -> new CustomException(ErrorCode.MARKER_NOT_FOUND)
        );
    }

    public MarkerLocationResponseDto getPosition(Long markerId) {
        Marker marker = findMarker(markerId);
        MarkerLocationResponseDto responseDto = new MarkerLocationResponseDto(marker.getLatitude(), marker.getLatitude());

        return responseDto;
    }

    @Transactional
    public MarkerResponseDto updateMarkerContent(Long markerId, MarkerContentRequestDto requestDto) {
        Marker marker = findMarker(markerId);
        marker.updateContent(requestDto);

        return new MarkerResponseDto(marker, null);
    }

    @Transactional
    public void deleteMarker(User user, Long markerId) {
        Marker marker = findMarker(markerId);

        if (!userService.isSameUser(user, marker.getDailyPlan().getSchedule().getUser())) {
            throw new CustomException(ErrorCode.NOT_MARKER_WRITER);
        }

        marker.delete();
        markerRepository.save(marker);
    }


    public boolean isMarkerExist(Long dailyPlanId) {
        List<MarkerResponseDto> markers = markerRepository.findByDailyPlanId(dailyPlanId);

        return !markers.isEmpty();
    }

    public Marker findMarker(Long markerId) {
        Marker marker = markerRepository.findById(markerId).orElseThrow(()
                -> new CustomException(ErrorCode.MARKER_NOT_FOUND)
        );

        if (marker.getStatus() == MarkerStatusEnum.DELETED) {
            throw new CustomException(ErrorCode.ALREADY_IS_DELETED);
        }

        return marker;
    }

    @Transactional
    public List<MarkerMoveResponseDto> moveMarker(Long dailyPlanId, Long markerId, MarkerMoveRequestDto requestDto, User user) {
        DailyPlan dailyPlan = dailyPlanService.findDailyPlanById(dailyPlanId);
        Marker checkMarker = findMarker(markerId);

        if (!userService.isSameUser(user, checkMarker.getDailyPlan().getSchedule().getUser())) {
            throw new CustomException(ErrorCode.NOT_MARKER_WRITER);
        }

        Long currentIndex = checkMarker.getMarkerIndex();
        Long targetIndex = requestDto.getMarkerIndex();

        if (currentIndex.equals(targetIndex)) {
            return null;
        }

        List<Marker> markersList = markerRepository.findAllByDailyPlanOrderByMarkerIndex(dailyPlan);

        Marker markerToMove = markersList.stream()
                .filter(marker -> marker.getId().equals(markerId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.MOVE_MARKER_NOT_FOUND));

        if (targetIndex < 0 || targetIndex >= markersList.size()) {
            throw new CustomException(ErrorCode.OVER_INDEX);
        }

        markersList.remove(markerToMove);
        markersList.add(Math.toIntExact(targetIndex), markerToMove);

        List<MarkerMoveResponseDto> responseList = new ArrayList<>();

        for (int i = 0; i < markersList.size(); i++) {
            Marker markers = markersList.get(i);
            markers.updateIndex(i);
            markerRepository.save(markers);

            responseList.add(new MarkerMoveResponseDto(markers.getId(), (long) i));
        }

        return responseList;
    }

}
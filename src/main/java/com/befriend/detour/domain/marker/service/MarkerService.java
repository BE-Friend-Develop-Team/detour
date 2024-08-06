package com.befriend.detour.domain.marker.service;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.dailyplan.service.DailyPlanService;
import com.befriend.detour.domain.file.repository.FileRepository;
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
import org.apache.coyote.BadRequestException;
import org.hibernate.annotations.Columns;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Marker marker = new Marker(requestDto.getLatitude(), requestDto.getLongitude(), dailyPlan, place);
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
    public void moveMarker(Long dailyPlanId, Long markerId, MarkerMoveRequestDto requestDto, User user) {
        DailyPlan dailyPlan = dailyPlanService.findDailyPlanById(dailyPlanId);
        Marker checkMarker = findMarker(markerId);

        if (!userService.isSameUser(user, checkMarker.getDailyPlan().getSchedule().getUser())) {
            throw new CustomException(ErrorCode.NOT_MARKER_WRITER);
        }

        // 요청으로부터 이동할 마커의 인덱스와 목표 인덱스를 가져옴
        Long currentIndex = checkMarker.getMarkerIndex();
        Long targetIndex = requestDto.getMarkerIndex();

        // 현재 인덱스와 목표 인덱스가 같으면 아무 작업도 하지 않음
        if (currentIndex.equals(targetIndex)) {
            return;
        }

        // 모든 마커를 현재 데일리플랜에서 조회
        List<Marker> markersList = markerRepository.findAllByDailyPlanOrderByMarkerIndex(dailyPlan);

        // 이동할 마커 찾기
        Marker markerToMove = markersList.stream()
                .filter(marker -> marker.getId().equals(markerId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.MOVE_MARKER_NOT_FOUND));

        // 목표 인덱스 범위 확인
        if (targetIndex < 0 || targetIndex >= markersList.size()) {
            throw new CustomException(ErrorCode.OVER_INDEX);
        }

        // 현재 인덱스에서 제거하고 목표 인덱스에 추가
        markersList.remove(markerToMove);
        markersList.add(Math.toIntExact(targetIndex), markerToMove);

        // 변경된 순서대로 모든 마커 저장
        for (int i = 0; i < markersList.size(); i++) {
            Marker markers = markersList.get(i);
            markers.updateIndex(i);
            markerRepository.save(markers);
        }

    }
}
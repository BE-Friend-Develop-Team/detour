package com.befriend.detour.domain.marker.service;

import com.befriend.detour.domain.marker.dto.MarkerContentRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.entity.Marker;
import com.befriend.detour.domain.marker.entity.MarkerStatusEnum;
import com.befriend.detour.domain.marker.repository.MarkerRepository;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import com.befriend.detour.domain.user.service.UserService;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final UserService userService;
//    private final DailyPlanService dailyPlanService;
//    private final PlaceService placeService;

    // 마커 생성
    @Transactional
    public MarkerResponseDto createMarker(User user, Long dailyPlanId, Long placeId, MarkerRequestDto requestDto) {
        /*
        - 지도에서 장소를 검색하고 선택한 후에 저장버튼을 누르면 마커 생성
        - 프론트에서 placeId를 넘겨주면 해당 아이디로 place와 연관관계 설정
         */
        validateActiveUser(user);

//        DailyPlan dailyPlan = dailyPlanService.findDailyPlanById(dailPlanId); // 데일리플랜 서비스 생성 후 수정
//        Place place = placeService.findPlaceById(placeId);

        Marker marker = new Marker(requestDto.getLatitude(), requestDto.getLongitude(), dailyPlan, place);
        markerRepository.save(marker);

        return new MarkerResponseDto(marker);
    }

    // 마커 전체 조회
    public List<MarkerResponseDto> getAllMarker(User user, Long dailyPlanId) {

       validateActiveUser(user);

        // 해당 데일리 플랜이 존재하는지 메서드 필요

        if (!isMarkerExist(dailyPlanId)) {
            throw new CustomException(ErrorCode.MARKER_NOT_FOUND_IN_DAILY_PLAN);
        }

        return markerRepository.findByDailyPlanId(dailyPlanId);
    }

    // 마커 단건 조회
    public MarkerResponseDto getMarker(User user, Long dailyPlanId, Long markerId) {

        validateActiveUser(user);

        // 해당 데일리 플랜이 존재하는지 메서드 필요

        if (!isMarkerExist(dailyPlanId)) {
            throw new CustomException(ErrorCode.MARKER_NOT_FOUND_IN_DAILY_PLAN);
        }

        Marker marker = markerRepository.findByIdAndDailyPlanId(markerId, dailyPlanId).orElseThrow(
                () -> new CustomException(ErrorCode.MARKER_NOT_FOUND)
        );

        return new MarkerResponseDto(marker);
    }

    // 마커 글 생성
    @Transactional
    public MarkerResponseDto updateMarkerContent(User user, Long markerId, MarkerContentRequestDto requestDto) {

        validateActiveUser(user);
        Marker marker = findMarkerById(markerId);
        marker.updateContent(requestDto);
        return new MarkerResponseDto(marker);

    }

    // 마커 삭제
    @Transactional
    public void deleteMarker(User user, Long markerId) {
        validateActiveUser(user);
        Marker marker = findMarker(user, markerId);

        if (!isSameUser(user, marker.getDailyPlan().getSchedule().getUser())) {
            throw new CustomException(ErrorCode.NOT_MARKER_WRITER);
        }

        marker.delete();
        markerRepository.save(marker);

    }

    // ID로 마커 찾기
    public Marker findMarkerById(Long markerId) {
        return markerRepository.findById(markerId).orElseThrow(() ->
                new CustomException(ErrorCode.MARKER_NOT_FOUND));
    }

    // dailyPlanId로 존재하는 마커인지 찾기
    public boolean isMarkerExist(Long dailyPlanId) {
        List<MarkerResponseDto> markers = markerRepository.findByDailyPlanId(dailyPlanId);
        return !markers.isEmpty();
    }

    // 동일한 유저인지 비교하기
    private boolean isSameUser(User user1, User user2) {
        return user1.getNickname().equals(user2.getNickname());
    }

    // 유저 권한 확인 (ACTIVE = true)
    private boolean isActiveUser(User user) {
        return user.getStatus().equals(UserStatusEnum.ACTIVE);
    }

    private void validateActiveUser(User user) {
        if (!isActiveUser(user)) {
            throw new CustomException(ErrorCode.USER_NOT_ACTIVE);
        }
    }

    public Marker findMarker(User user, Long markerId)
    {
        validateActiveUser(user);

        Marker marker = markerRepository.findById(markerId).orElseThrow(()
                -> new CustomException(ErrorCode.MARKER_NOT_FOUND)
        );

        if(marker.getStatus() == MarkerStatusEnum.DELETED)
        {
            throw new CustomException(ErrorCode.ALREADY_IS_DELETED);
        }

        return marker;
    }

}

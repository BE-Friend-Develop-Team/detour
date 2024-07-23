package com.befriend.detour.domain.marker.service;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.marker.dto.MarkerContentRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.entity.Marker;
import com.befriend.detour.domain.marker.repository.MarkerRepository;
import com.befriend.detour.domain.place.entity.Place;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.service.UserService;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final UserService userService;
    private final DailyPlanService dailyPlanService;
    private final PlaceService placeService;

    // 마커 생성
    public MarkerResponseDto createMarker(String nickname, Long dailPlanId, Long placeId, MarkerRequestDto requestDto) {
        /*
        - 지도에서 장소를 검색하고 선택한 후에 저장버튼을 누르면 마커 생성
        - 프론트에서 placeId를 넘겨주면 해당 아이디로 place와 연관관계 설정
         */
        User user = userService.findUserByNickName(nickname);
        DailyPlan dailyPlan = dailyPlanService.findDailyPlanById(dailPlanId); // 데일리플랜 서비스 생성 후 수정
        Place place = placeService.findPlaceById(placeId);

        Marker marker = new Marker(requestDto.getLatitude(), requestDto.getLongitude(), dailyPlan, place);
        markerRepository.save(marker);

        return new MarkerResponseDto(marker);
    }


    // 마커 글 생성
    @Transactional
    public MarkerResponseDto createMarkerContent(String nickname, Long markerId, MarkerContentRequestDto requestDto) {
        User user = userService.findUserByNickName(nickname);
        Marker marker = findMarkerById(markerId);

        if (user.getId().equals(marker.getDailyPlan().getSchedule().getUser().getId())) {
            marker.updateContent(requestDto);
        } else {
            throw new CustomException(ErrorCode.USER_NOT_MATCH_WITH_MARKER);
        }
        return new MarkerResponseDto(marker);
    }

    // ID로 마커 찾기
    public Marker findMarkerById(Long markerId) {
        return markerRepository.findById(markerId).orElseThrow(() ->
                new CustomException(ErrorCode.MARKER_NOT_FOUND));
    }
}

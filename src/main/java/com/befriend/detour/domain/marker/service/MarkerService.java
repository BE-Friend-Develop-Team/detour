package com.befriend.detour.domain.marker.service;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.marker.dto.MarkerContentRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerRequestDto;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.entity.Marker;
import com.befriend.detour.domain.marker.entity.MarkerStatusEnum;
import com.befriend.detour.domain.marker.repository.MarkerRepository;
import com.befriend.detour.domain.place.entity.Place;
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
    // TODO: DailyPlan 생성시 주석 제거 예정
//    private final DailyPlanService dailyPlanService;
//    private final PlaceService placeService;

    // 마커 생성  TODO: DailyPlan 생성시 주석 제거 예정
    @Transactional
    public MarkerResponseDto createMarker( Long dailyPlanId, Long placeId, MarkerRequestDto requestDto) {
         /*
        - 지도에서 장소를 검색하고 선택한 후에 저장버튼을 누르면 마커 생성
        - 프론트에서 placeId를 넘겨주면 해당 아이디로 place와 연관관계 설정
         */

      //  DailyPlan dailyPlan = dailyPlanService.findDailyPlanById(dailPlanId); // 데일리플랜 서비스 생성 후 수정
      //  Place place = placeService.findPlaceById(placeId);

       // Marker marker = new Marker(requestDto.getLatitude(), requestDto.getLongitude(), dailyPlan, place);
       // markerRepository.save(marker);

       //  return new MarkerResponseDto(marker);
        return null;
    }

    // 마커 전체 조회
    public List<MarkerResponseDto> getAllMarker(Long dailyPlanId) {

        // 해당 데일리 플랜이 존재하는지 메서드 필요

        if (!isMarkerExist(dailyPlanId)) {
            throw new CustomException(ErrorCode.MARKER_NOT_FOUND_IN_DAILY_PLAN);
        }

        return markerRepository.findByDailyPlanId(dailyPlanId);
    }

    // 마커 단건 조회
    public MarkerResponseDto getMarker( Long dailyPlanId, Long markerId) {


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
    public MarkerResponseDto updateMarkerContent(Long markerId, MarkerContentRequestDto requestDto) {

        Marker marker = findMarker(markerId);
        marker.updateContent(requestDto);
        return new MarkerResponseDto(marker);

    }

    // 마커 삭제
    @Transactional
    public void deleteMarker(User user, Long markerId) {

        Marker marker = findMarker(markerId);

        if (!isSameUser(user, marker.getDailyPlan().getSchedule().getUser())) {
            throw new CustomException(ErrorCode.NOT_MARKER_WRITER);
        }

        marker.delete();
        markerRepository.save(marker);

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

    public Marker findMarker(Long markerId) {

        Marker marker = markerRepository.findById(markerId).orElseThrow(()
                -> new CustomException(ErrorCode.MARKER_NOT_FOUND)
        );

        if(marker.getStatus() == MarkerStatusEnum.DELETED) {
            throw new CustomException(ErrorCode.ALREADY_IS_DELETED);
        }

        return marker;

    }

}

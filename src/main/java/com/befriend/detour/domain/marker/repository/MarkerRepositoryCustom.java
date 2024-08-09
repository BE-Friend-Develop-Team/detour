package com.befriend.detour.domain.marker.repository;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.entity.Marker;

import java.util.List;
import java.util.Optional;

public interface MarkerRepositoryCustom {

    List<MarkerResponseDto> findByDailyPlanId(Long dailyPlanId);

    Optional<Marker> findByIdAndDailyPlanId(Long markerId, Long dailyPlanId);

    Optional<MarkerResponseDto> fetchMarkerDetails(Long markerId, Long dailyPlanId);

    List<Marker> findAllByDailyPlanOrderByMarkerIndex(DailyPlan dailyPlan);

    Long findMaxMarkerIndexByDailyPlan(DailyPlan dailyPlan);

}

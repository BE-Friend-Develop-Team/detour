package com.befriend.detour.domain.marker.repository;

import com.befriend.detour.domain.marker.entity.Marker;

import java.util.List;

public interface MarkerRepositoryCustom {
    List<Marker> findByDailyPlanId(Long dailyPlanId);

}

package com.befriend.detour.domain.marker.repository;

import com.befriend.detour.domain.marker.entity.Marker;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.befriend.detour.domain.marker.entity.QMarker.marker;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MarkerRepositoryImpl implements MarkerRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Marker> findByDailyPlanId(Long dailyPlanId)
    {
        return  jpaQueryFactory.selectFrom(marker)
                .where(marker.dailyPlan.id.eq(dailyPlanId))
                .fetch();

    }

}

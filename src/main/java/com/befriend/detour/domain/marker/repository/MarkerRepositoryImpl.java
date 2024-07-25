package com.befriend.detour.domain.marker.repository;

import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.entity.Marker;
import com.befriend.detour.domain.marker.entity.MarkerStatusEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.befriend.detour.domain.marker.entity.QMarker.marker;

@Repository
@RequiredArgsConstructor
public class MarkerRepositoryImpl implements MarkerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MarkerResponseDto> findByDailyPlanId(Long dailyPlanId) {

        return jpaQueryFactory.selectFrom(marker)
                .where(marker.dailyPlan.id.eq(dailyPlanId),
                        marker.status.ne(MarkerStatusEnum.DELETED))
                .orderBy(marker.id.asc())
                .fetch()
                .stream()
                .map(MarkerResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Marker> findByIdAndDailyPlanId(Long markerId, Long dailyPlanId) {
        Marker result = jpaQueryFactory.selectFrom(marker)
                .where(marker.id.eq(markerId)
                        .and(marker.dailyPlan.id.eq(dailyPlanId)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
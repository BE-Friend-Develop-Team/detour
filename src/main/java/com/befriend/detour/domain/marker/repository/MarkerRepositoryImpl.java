package com.befriend.detour.domain.marker.repository;

import com.befriend.detour.domain.dailyplan.entity.DailyPlan;
import com.befriend.detour.domain.file.entity.File;
import com.befriend.detour.domain.file.repository.FileRepository;
import com.befriend.detour.domain.marker.dto.MarkerResponseDto;
import com.befriend.detour.domain.marker.entity.Marker;
import com.befriend.detour.domain.marker.entity.MarkerStatusEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.befriend.detour.domain.marker.entity.QMarker.marker;

@Repository
@RequiredArgsConstructor
public class MarkerRepositoryImpl implements MarkerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final FileRepository fileRepository;

    @Override
    public List<MarkerResponseDto> findByDailyPlanId(Long dailyPlanId) {

        return jpaQueryFactory.selectFrom(marker)
                .where(marker.dailyPlan.id.eq(dailyPlanId),
                        marker.status.ne(MarkerStatusEnum.DELETED))
                .orderBy(marker.id.asc())
                .fetch()
                .stream()
                .map(this::toMarkerResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Marker> findAllByDailyPlanOrderByMarkerIndex(DailyPlan dailyPlan) {

        return new ArrayList<>(jpaQueryFactory.selectFrom(marker)
                .where(marker.dailyPlan.eq(dailyPlan),
                        marker.status.ne(MarkerStatusEnum.DELETED))
                .orderBy(marker.id.asc())
                .fetch());
    }

    @Override
    public Long findMaxMarkerIndexByDailyPlan(DailyPlan dailyPlan) {

        return jpaQueryFactory
                .select(marker.markerIndex.max())
                .from(marker)
                .where(marker.dailyPlan.eq(dailyPlan)
                        .and(marker.status.ne(MarkerStatusEnum.DELETED)))
                .fetchOne();
    }

    @Override
    public Optional<MarkerResponseDto> fetchMarkerDetails(Long markerId, Long dailyPlanId) {

        return findByIdAndDailyPlanId(markerId, dailyPlanId)
                .map(this::toMarkerResponseDto);
    }

    @Override
    public Optional<Marker> findByIdAndDailyPlanId(Long markerId, Long dailyPlanId) {
        Marker result = jpaQueryFactory.selectFrom(marker)
                .where(marker.id.eq(markerId)
                        .and(marker.dailyPlan.id.eq(dailyPlanId)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    private MarkerResponseDto toMarkerResponseDto(Marker marker) {
        List<String> imageUrls = fileRepository.findByMarkerId(marker.getId())
                .stream()
                .map(File::getFileUrl)
                .collect(Collectors.toList());

        return new MarkerResponseDto(marker, imageUrls);
    }

}
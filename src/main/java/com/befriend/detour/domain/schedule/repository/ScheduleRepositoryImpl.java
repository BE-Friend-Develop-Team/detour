package com.befriend.detour.domain.schedule.repository;

import com.befriend.detour.domain.invitation.entity.QInvitation;
import com.befriend.detour.domain.schedule.entity.QSchedule;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<Schedule>> findSchedulesByCreatedUser(Long userId, Pageable pageable) {
        QSchedule schedule = QSchedule.schedule;
        QInvitation invitation = QInvitation.invitation;

        List<Schedule> schedules = jpaQueryFactory.selectFrom(schedule)
                .where(schedule.user.id.eq(userId)
                        .or(schedule.id.in(
                                jpaQueryFactory.select(invitation.schedule.id)
                                        .from(invitation)
                                        .where(invitation.user.id.eq(userId))
                        )))
                .orderBy(schedule.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return Optional.ofNullable(schedules);
    }

    @Override
    public Optional<List<Long>> getScheduleIdRanking() {
        QSchedule schedule = QSchedule.schedule;

        List<Long> scheduleIds = jpaQueryFactory.select(schedule.id)
                .from(schedule)
                .orderBy(schedule.hourHits.desc())
                .limit(12)
                .fetch();

        return Optional.ofNullable(scheduleIds);
    }

    @Override
    @Transactional
    public void deleteAllHourHits() {
        QSchedule schedule = QSchedule.schedule;

        jpaQueryFactory.update(schedule)
                .set(schedule.hourHits, 0L)
                .execute();
    }

    @Override
    public Page<Schedule> findAllBySearch(Pageable pageable, String search) {
        QSchedule schedule = QSchedule.schedule;

        // Sort 객체에서 정렬 방향과 속성 추출
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<?> pathBuilder = new PathBuilder<>(schedule.getType(), schedule.getMetadata());
            StringPath path = pathBuilder.getString(order.getProperty());
            OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                    order.isAscending() ? Order.ASC : Order.DESC, path
            );
            orderSpecifiers.add(orderSpecifier);
        }

        // 총 개수 조회
        long total = jpaQueryFactory.selectFrom(schedule)
                .where(schedule.title.contains(search))
                .fetchCount();

        // 페이징된 데이터 조회
        List<Schedule> schedules = jpaQueryFactory.selectFrom(schedule)
                .where(schedule.title.contains(search))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Page 객체 생성 및 반환
        return new PageImpl<>(schedules, pageable, total);
    }
}

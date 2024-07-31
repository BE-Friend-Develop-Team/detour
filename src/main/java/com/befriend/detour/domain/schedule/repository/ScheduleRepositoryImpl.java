package com.befriend.detour.domain.schedule.repository;

import com.befriend.detour.domain.invitation.entity.QInvitation;
import com.befriend.detour.domain.schedule.entity.QSchedule;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

}

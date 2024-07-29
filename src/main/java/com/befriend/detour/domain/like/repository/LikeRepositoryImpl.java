package com.befriend.detour.domain.like.repository;

import com.befriend.detour.domain.like.entity.Like;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.befriend.detour.domain.like.entity.QLike.like;
import static com.befriend.detour.domain.schedule.entity.QSchedule.schedule;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByUserAndSchedule(User user, Schedule schedule) {

        return jpaQueryFactory
                .selectFrom(like)
                .where(like.user.eq(user)
                        .and(like.schedule.eq(schedule)))
                .fetchFirst() != null;
    }

    @Override
    public Like findLikeWithSchedule(Long likeId) {

        return jpaQueryFactory
                .selectFrom(like)
                .leftJoin(like.schedule, schedule)
                .where(like.id.eq(likeId))
                .fetchOne();
    }

    @Override
    public Optional<List<Like>> getUserLikedSchedules(User user, Pageable pageable) {
        List<Like> likes = jpaQueryFactory.selectFrom(like)
                .where(like.user.eq(user))
                .orderBy(like.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return Optional.ofNullable(likes);
    }

}
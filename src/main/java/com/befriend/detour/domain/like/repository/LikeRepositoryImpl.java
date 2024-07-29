package com.befriend.detour.domain.like.repository;

import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.befriend.detour.domain.like.entity.QLike.like;

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

}
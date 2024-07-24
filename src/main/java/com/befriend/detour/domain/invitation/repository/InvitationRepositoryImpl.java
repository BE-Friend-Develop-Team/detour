package com.befriend.detour.domain.invitation.repository;

import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.befriend.detour.domain.invitation.entity.QInvitation.invitation;

@Repository
@RequiredArgsConstructor
public class InvitationRepositoryImpl implements InvitationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean isMemberOfSchedule(Schedule schedule, User user){
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(invitation)
                .where(invitation.schedule.eq(schedule)
                        .and(invitation.user.eq(user)))
                .fetchFirst();

        return fetchOne != null;
    }

}
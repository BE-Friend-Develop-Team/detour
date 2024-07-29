package com.befriend.detour.domain.invitation.repository;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.invitation.entity.QInvitation;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InvitationRepositoryImpl implements InvitationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QInvitation qInvitation = QInvitation.invitation;

    @Override
    public Optional<Invitation> findInvitationByScheduleAndUser(Schedule schedule, User user){
        Invitation invitation = jpaQueryFactory
                .selectFrom(qInvitation)
                .where(qInvitation.schedule.eq(schedule)
                        .and(qInvitation.user.eq(user)))
                .fetchOne();

        return Optional.ofNullable(invitation);
    }

    @Override
    public boolean existsByScheduleAndUser(Schedule schedule, User user) {

        return jpaQueryFactory
                .selectFrom(qInvitation)
                .where(qInvitation.schedule.eq(schedule)
                        .and(qInvitation.user.eq(user)))
                .fetchFirst() != null;
    }

}
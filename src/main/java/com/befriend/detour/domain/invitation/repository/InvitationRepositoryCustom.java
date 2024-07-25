package com.befriend.detour.domain.invitation.repository;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepositoryCustom {
    Invitation findInvitationByScheduleAndUser(Schedule schedule, User user);
}

package com.befriend.detour.domain.invitation.repository;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepositoryCustom {

    Optional<Invitation> findInvitationByScheduleAndUser(Schedule schedule, User user);
    boolean existsByScheduleAndUser(Schedule schedule, User user);
    Optional<List<User>> findUsersByScheduleId(Long scheduleId);

}

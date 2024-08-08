package com.befriend.detour.domain.invitation.repository;

import com.befriend.detour.domain.invitation.entity.Invitation;
import com.befriend.detour.domain.schedule.entity.Schedule;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long>, InvitationRepositoryCustom {

    default void checkIfMemberOfSchedule(Schedule schedule, User user) {
        if (!existsByScheduleAndUser(schedule, user)) {
            throw new CustomException(ErrorCode.USER_NOT_MEMBER);
        }
    }

    Optional<List<User>> findUsersByScheduleId(Long scheduleId);

}

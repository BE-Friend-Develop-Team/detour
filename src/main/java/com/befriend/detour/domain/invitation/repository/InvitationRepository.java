package com.befriend.detour.domain.invitation.repository;

import com.befriend.detour.domain.invitation.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long>, InvitationRepositoryCustom {
}

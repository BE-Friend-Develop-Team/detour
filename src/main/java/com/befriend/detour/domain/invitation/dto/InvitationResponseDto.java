package com.befriend.detour.domain.invitation.dto;

import com.befriend.detour.domain.invitation.entity.Invitation;
import lombok.Getter;

@Getter
public class InvitationResponseDto {
    private String nickname;

    public InvitationResponseDto(Invitation invitation) {
        this.nickname = invitation.getUser().getNickname();
    }
}

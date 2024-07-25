package com.befriend.detour.domain.user.dto;

import com.befriend.detour.domain.user.entity.UserRoleEnum;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private Long id;
    private String loginId;
    private Long kakaoId;
    private String email;
    private String nickname;
    private UserStatusEnum status;
    private UserRoleEnum role;

    public ProfileResponseDto(Long id, String loginId, Long kakaoId, String email, String nickname, UserStatusEnum status, UserRoleEnum role) {
        this.id = id;
        this.loginId = loginId;
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
        this.status = status;
        this.role = role;
    }

}

package com.befriend.detour.domain.user.dto;

import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserRoleEnum;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private Long userId;
    private String email;
    private String loginId;
    private Long kakaoId;
    private String nickname;

    public LoginResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.loginId = user.getLoginId();
        this.kakaoId = user.getKakaoId();
        this.nickname = user.getNickname();
    }

}
package com.befriend.detour.domain.user.dto;

import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private Long id;
    private String loginId;
    private Long kakaoId;
    private String email;
    private String nickname;

    public ProfileResponseDto(Long id, String loginId, Long kakaoId, String email, String nickname) {
        this.id = id;
        this.loginId = loginId;
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
    }
}

package com.befriend.detour.domain.user.dto;

import com.befriend.detour.domain.user.entity.User;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private User user;

    public LoginResponseDto(User user) {
        this.user = user;
    }

}
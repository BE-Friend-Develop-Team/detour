package com.befriend.detour.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EditNicknameRequestDto {

    private String nickname;

    public EditNicknameRequestDto(String nickname) {
        this.nickname = nickname;
    }

}

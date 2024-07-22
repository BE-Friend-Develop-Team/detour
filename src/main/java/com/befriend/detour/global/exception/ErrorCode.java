package com.befriend.detour.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATE_NICKNAME(500, "이미 사용중인 닉네임입니다."),
    DUPLICATE_USER_ID(500,"이미 사용중인 ID입니다."),
    DUPLICATE_EMAIL(500,"이미 사용중인 EMAIL입니다."),
    USER_NOT_FOUND(404, "존재하지 않는 사용자입니다.");

    private int status;
    private String message;

}
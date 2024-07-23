package com.befriend.detour.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // user 관련 오류 처리
    DUPLICATE_NICKNAME(500, "이미 사용중인 닉네임입니다."),
    DUPLICATE_USER_ID(500,"이미 사용중인 ID입니다."),
    DUPLICATE_EMAIL(500,"이미 사용중인 EMAIL입니다."),
    USER_NOT_FOUND(404, "존재하지 않는 사용자입니다."),
    LOGIN_FAIL(404, "로그인에 실패했습니다."),
    WRONG_HTTP_REQUEST(500, "잘못된 http 요청입니다."),
    USER_NOT_ACTIVE(500, "현재 활동 가능한 사용자가 아닙니다."),

    // marker 관련 오류 처리
    MARKER_NOT_FOUND(400, "해당 데일리 플랜에 대한 마커가 없습니다."),
    USER_NOT_MATCH_WITH_MARKER (404, "해당 마커의 작성자가 아닙니다.");

    private int status;
    private String message;

}
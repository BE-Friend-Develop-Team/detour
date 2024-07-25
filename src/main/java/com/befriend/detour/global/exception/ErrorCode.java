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
    USER_NOT_ACTIVE(500, "차단된 사용자입니다."),
    INCORRECT_PASSWORD(500, "현재 비밀번호가 일치하지 않습니다."),
    CONFIRM_NEW_PASSWORD_NOT_MATCH(500, "새로운 비밀번호가 서로 일치하지 않습니다."),
    NO_MEMBERS_FOUND(404, "회원이 없습니다."),

    // schedule 관련 오류 처리
    SCHEDULE_NOT_FOUND(404, "존재하지 않는 일정입니다."),
    NOT_SCHEDULE_MEMBER(404, "해당 일정의 일행이어야 일정에 대한 권한이 있습니다."),
    ALREADY_INVITED(409, "이미 해당 일정에 초대된 사용자입니다."),
    USER_NOT_MEMBER(404, "해당 일정에 존재하지 않는 일행입니다."),

    // daily plan 관련 오류 처리
    DAILY_PLAN_NOT_FOUND(404, "존재하지 않는 데일리플랜입니다."),

    // marker 관련 오류 처리
    NOT_MARKER_WRITER(404, "마커 작성자만 삭제 가능합니다."),
    MARKER_NOT_FOUND_IN_DAILY_PLAN(404, "해당 데일리 플랜에 대한 마커가 없습니다."),
    MARKER_NOT_FOUND(404,"마커를 찾을 수 없습니다."),
    ALREADY_IS_DELETED(404,"이미 차단된 마커입니다."),
    USER_NOT_MATCH_WITH_MARKER (404, "해당 마커의 작성자가 아닙니다."),

    // place 관련 오류 처리
    PLACE_NOT_FOUND(404, "장소를 찾을 수 없습니다.");

    private int status;
    private String message;

}
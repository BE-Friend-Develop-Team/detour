package com.befriend.detour.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // redisson
    LOCK_ACQUISITION_FAILED(409,"잠금 획득을 실패했습니다."),
    //s3
    PUT_OBJECT_EXCEPTION(500, "s3 업로드에 문제가 발생했습니다."),
    FILE_NAME_INVALID(400, "잘못된 파일명입니다."),
    EXTENSION_IS_EMPTY(404, "파일을 찾을 수 없습니다."),
    EXTENSION_INVALID(404, "잘못된 확장자명입니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(404, "업로드에 문제가 발생했습니다."),
    NULL_MULTIPART_FILES_EXCEPTION(404, "파일 업로드 요청에서 파일이 포함되지 않았습니다. 파일을 선택하고 다시 시도하십시오."),

    // user 관련 오류 처리
    DUPLICATE_NICKNAME(500, "이미 사용중인 닉네임입니다."),
    DUPLICATE_USER_ID(500, "이미 사용중인 ID입니다."),
    DUPLICATE_EMAIL(500, "이미 사용중인 EMAIL입니다."),
    USER_NOT_FOUND(404, "존재하지 않는 사용자입니다."),
    LOGIN_FAIL(404, "로그인에 실패했습니다."),
    WRONG_HTTP_REQUEST(500, "잘못된 http 요청입니다."),
    USER_NOT_ACTIVE(500, "차단된 사용자입니다."),
    INCORRECT_PASSWORD(500, "현재 비밀번호가 일치하지 않습니다."),
    CONFIRM_NEW_PASSWORD_NOT_MATCH(500, "새로운 비밀번호가 서로 일치하지 않습니다."),
    NO_USERS_FOUND(404, "회원이 없습니다."),
    REFRESH_TOKEN_NOT_VALIDATE(500, "리프레시 토큰이 만료 되었거나 잘못되었습니다."),
    VERIFY_NOT_ALLOWED(400, "인증 요청이 잘못 되었습니다."),

    // schedule 관련 오류 처리
    SCHEDULE_NOT_FOUND(404, "존재하지 않는 일정입니다."),
    ALREADY_INVITED(409, "이미 해당 일정에 초대된 사용자입니다."),
    USER_NOT_MEMBER(404, "해당 일정에 존재하지 않는 일행입니다."),
    USER_CREATED_SCHEDULES_NOT_FOUND(404, "작성한 일정이 없습니다."),
    USER_LIKED_SCHEDULES_NOT_FOUND(404, "좋아요한 일정이 없습니다."),
    SORT_NOT_FOUND(404, "찾을 수 없는 정렬입니다."),
    INVITATION_NOT_FOUND(404, "존재하지 않는 초대입니다."),

    // comment 관련 오류 처리
    COMMENT_NOT_FOUND(404, "존재하지 않는 일정입니다."),
    USER_NOT_MATCH_WITH_COMMENT(404, "해당 댓글의 작성자가 아니면 수정이 불가합니다."),

    // like 관련 오류 처리
    ALREADY_LIKED(409, "이미 좋아요를 누른 일정입니다."),
    LIKE_NOT_EXIST(404, "해당 좋아요가 존재하지 않습니다."),
    CANNOT_CANCEL_OTHERS_LIKE(403, "다른 사람의 좋아요는 취소할 수 없습니다."),
    UNAUTHORIZED_ACTION(404, "승인되지 않은 접근입니다."),

    // daily plan 관련 오류 처리
    DAILY_PLAN_NOT_FOUND(404, "존재하지 않는 데일리플랜입니다."),

    // marker 관련 오류 처리
    NOT_MARKER_WRITER(404, "마커 작성자만 삭제 가능합니다."),
    MARKER_NOT_FOUND_IN_DAILY_PLAN(404, "해당 데일리 플랜에 대한 마커가 없습니다."),
    MARKER_NOT_FOUND(404, "마커를 찾을 수 없습니다."),
    ALREADY_IS_DELETED(404, "이미 차단된 마커입니다."),
    USER_NOT_MATCH_WITH_MARKER(404, "해당 마커의 작성자가 아닙니다."),
    OVER_INDEX(500, "목표 인덱스가 범위를 벗어납니다."),
    MOVE_MARKER_NOT_FOUND(500, "이동할 컬럼을 찾을 수 없습니다."),

    // place 관련 오류 처리
    PLACE_NOT_FOUND(404, "장소를 찾을 수 없습니다."),

    // redis 관련 오류 처리
    REDIS_NOT_CONNECT(500, "레디스 서버에 연결할 수 없습니다.");

    private int status;
    private String message;

}
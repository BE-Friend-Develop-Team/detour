package com.befriend.detour.global.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "CustomException:: ")
public class CustomException extends RuntimeException {

    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {

        return errorCode;
    }

}

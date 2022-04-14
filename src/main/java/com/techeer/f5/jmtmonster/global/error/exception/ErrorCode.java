package com.techeer.f5.jmtmonster.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_JSON_FORMAT"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE"),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED"),
    ILLEGAL_STATE(HttpStatus.BAD_REQUEST, "ILLEGAL_STATE"),
    EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "EXCEPTION"),
            ;

    private final HttpStatus status;
    private final String code;
}

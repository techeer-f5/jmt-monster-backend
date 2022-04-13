package com.techeer.f5.jmtmonster.global.error.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomStatusException extends RuntimeException {

    private final ErrorCode code;

    public CustomStatusException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}

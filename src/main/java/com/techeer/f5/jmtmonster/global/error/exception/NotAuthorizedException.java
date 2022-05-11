package com.techeer.f5.jmtmonster.global.error.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotAuthorizedException extends CustomStatusException {
    public NotAuthorizedException(String reason) {

        super(ErrorCode.NOT_AUTHORIZED, reason);
    }
}

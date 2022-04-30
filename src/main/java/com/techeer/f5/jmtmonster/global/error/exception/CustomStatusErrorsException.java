package com.techeer.f5.jmtmonster.global.error.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@Setter
@Getter
public abstract class CustomStatusErrorsException extends CustomStatusException {

    private final List<FieldError> errors;

    protected CustomStatusErrorsException(ErrorCode code, String message, List<FieldError> errors) {
        super(code, message);
        this.errors= errors;
    }
}

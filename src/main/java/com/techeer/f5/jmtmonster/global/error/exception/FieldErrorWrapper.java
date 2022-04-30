package com.techeer.f5.jmtmonster.global.error.exception;

import org.springframework.validation.FieldError;

public class FieldErrorWrapper extends FieldError {

    public FieldErrorWrapper(String objectName, String field, Object rejectedValue,
            String defaultMessage) {
        super(objectName, field, rejectedValue, false, null, null, defaultMessage);
    }
}

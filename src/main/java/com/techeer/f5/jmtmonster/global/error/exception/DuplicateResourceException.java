package com.techeer.f5.jmtmonster.global.error.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@Getter
@Setter
public class DuplicateResourceException extends CustomStatusErrorsException {

    public DuplicateResourceException(String resourceName, List<FieldError> errors) {
        super(ErrorCode.CONFLICT, resourceName + " already exists with given condition.", errors);
    }
}

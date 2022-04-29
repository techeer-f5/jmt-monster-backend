package com.techeer.f5.jmtmonster.global.error.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@Setter
@Getter
public class InnerResourceNotFoundException extends CustomStatusErrorsException {

    public InnerResourceNotFoundException(String resourceName, List<FieldError> errors) {
        super(ErrorCode.INNER_RESOURCE_NOT_FOUND,
                resourceName + " cannot be processed with given condition.", errors);
    }
}

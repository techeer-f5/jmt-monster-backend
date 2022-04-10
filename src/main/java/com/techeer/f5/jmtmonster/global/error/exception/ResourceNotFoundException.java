package com.techeer.f5.jmtmonster.global.error.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResourceNotFoundException extends CustomStatusException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object value) {

        super(ErrorCode.NOT_FOUND, resourceName + " not found with " + fieldName + "=" + value.toString());
    }
}

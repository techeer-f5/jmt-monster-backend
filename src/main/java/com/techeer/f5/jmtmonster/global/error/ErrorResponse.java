package com.techeer.f5.jmtmonster.global.error;

import com.techeer.f5.jmtmonster.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class ErrorResponse {

    private String code;

    private String message;

    private List<CustomFieldError> errors;

    private LocalDateTime timestamp = LocalDateTime.now();

    private ErrorResponse(ErrorCode errorCode, String message, List<FieldError> errors) {
        setErrorCode(errorCode);
        setMessage(message);
        this.errors = errors.stream().map(CustomFieldError::new).collect(Collectors.toList());
    }

    private ErrorResponse(ErrorCode errorCode, String message, String exceptionMessage) {
        setErrorCode(errorCode);
        setMessage(message);
        this.errors = List.of(new CustomFieldError("", "", exceptionMessage));
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode, message, Collections.emptyList());
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, BindingResult bindingResult) {
        return new ErrorResponse(errorCode, message, bindingResult.getFieldErrors());
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, String exceptionMessage) {
        return new ErrorResponse(errorCode, message, exceptionMessage);
    }

    private void setErrorCode(ErrorCode errorCode) {
        this.code = errorCode.getCode();
    }

    @Getter
    @AllArgsConstructor
    public static class CustomFieldError {

        private String field;
        private String value;
        private String reason;

        private CustomFieldError(FieldError fieldError) {
            this.field = fieldError.getField();

            if (fieldError.getRejectedValue() != null) {
                this.value = fieldError.getRejectedValue().toString();
            } else {
                this.value = "";
            }
            this.reason = fieldError.getDefaultMessage();
        }
    }
}

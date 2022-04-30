package com.techeer.f5.jmtmonster.global.error;

import com.techeer.f5.jmtmonster.global.error.exception.CustomStatusErrorsException;
import com.techeer.f5.jmtmonster.global.error.exception.CustomStatusException;
import com.techeer.f5.jmtmonster.global.error.exception.DuplicateResourceException;
import com.techeer.f5.jmtmonster.global.error.exception.ErrorCode;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleMethodArgumentNotValidException {}", ex.getMessage());

        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode(), ex.getBindingResult());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleBindException {}", ex.getMessage());

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode(), ex.getBindingResult());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleNoHandlerFoundException {}", ex.getMessage());

        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException {}", ex.getMessage());

        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalStatementException(IllegalStateException e) {
        log.error("handleIllegalStateException {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.ILLEGAL_STATE;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode(), e.getMessage());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("handleIllegalArgumentException {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.ILLEGAL_ARGUMENT;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode(), e.getMessage());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("handleConstraintViolationException {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode(), e.getConstraintViolations());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleHttpMessageNotReadable {}", ex.getMessage());

        ErrorCode errorCode = ErrorCode.INVALID_JSON_FORMAT;
        final ErrorResponse response = ErrorResponse.of(errorCode, "Invalid JSON format.");
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(CustomStatusErrorsException.class)
    protected ResponseEntity<ErrorResponse> handleCustomStatusErrorsException(CustomStatusErrorsException e, WebRequest request) {
        log.error("handleCustomStatusErrorsException {}", e.getMessage());

        ErrorCode errorCode = e.getCode();
        final ErrorResponse response = ErrorResponse.of(errorCode, e.getMessage(), e.getErrors());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(CustomStatusException.class)
    protected ResponseEntity<ErrorResponse> handleCustomStatusException(CustomStatusException e, WebRequest request) {
        log.error("handleCustomStatusException {}", e.getMessage());

        ErrorCode errorCode = e.getCode();
        final ErrorResponse response = ErrorResponse.of(errorCode, e.getMessage());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.EXCEPTION;
        final ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getCode(), e.getMessage());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}

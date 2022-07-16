package com.techeer.f5.jmtmonster.domain.oauth.aop.aspect;

import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.global.error.ErrorResponse;
import com.techeer.f5.jmtmonster.global.error.exception.NotAuthorizedException;
import com.techeer.f5.jmtmonster.global.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthAspect {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final PersistentTokenRepository persistentTokenRepository;
    private final JsonMapper jsonMapper;

    @Pointcut("@annotation(com.techeer.f5.jmtmonster.domain.oauth.aop.annotation.AuthOnly)")
    private void checkAuth() {
    }

    private ResponseEntity<ErrorResponse> sendErrorResponse(String message) {
        NotAuthorizedException ex = new NotAuthorizedException(message);
        ErrorResponse errorResponse = ErrorResponse.of(ex.getCode(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @Around("checkAuth()")
    private Object checkAuthAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        UUID token;

        try {
            token = UUID.fromString(authorizationHeader.substring("Bearer ".length()));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | NullPointerException e) {
            return sendErrorResponse("토큰 UUID 파싱에 실패했습니다.");
        }

        if (persistentTokenRepository.findById(token).isEmpty()) {
            return sendErrorResponse("주어진 토큰이 올바르지 않습니다.");
        }

        return joinPoint.proceed();
    }
}

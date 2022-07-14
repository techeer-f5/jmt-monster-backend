package com.techeer.f5.jmtmonster.domain.oauth.aop.aspect;

import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.global.error.ErrorResponse;
import com.techeer.f5.jmtmonster.global.error.exception.ErrorCode;
import com.techeer.f5.jmtmonster.global.error.exception.NotAuthorizedException;
import com.techeer.f5.jmtmonster.global.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    private void sendErrorResponse(String message) {
        NotAuthorizedException ex = new NotAuthorizedException(message);
        ErrorResponse errorResponse = ErrorResponse.of(ex.getCode(), ex.getMessage());

        response.setStatus(ex.getCode().getStatus().value());
        response.setContentType("application/json");

        String result = jsonMapper.asJsonString(errorResponse);

        try {
            response.getWriter().write(result);
        } catch (IOException ignored) {
            log.error("Failed to set error response because of IOException");
        }
    }

    @Before("checkAuth()")
    private void checkAuthBefore(JoinPoint joinPoint) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        UUID token;

        try {
            token = UUID.fromString(authorizationHeader.substring("Bearer ".length()));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            sendErrorResponse("Failed to parse authorization header bearer token");
            return;
        }

        if (persistentTokenRepository.findById(token).isEmpty()) {
            sendErrorResponse("Token is not valid");
        }

    }
}

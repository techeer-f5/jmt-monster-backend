package com.techeer.f5.jmtmonster.global.aop.aspect;

import com.techeer.f5.jmtmonster.domain.user.service.UserService;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

// TODO: Talk about further migration on Spring security (Filter etc...)
@Component
@Aspect
@RequiredArgsConstructor
public class AuthOnlyAspect {
    // Easy way to inject HttpServletRequest on aspect.
    // See https://stackoverflow.com/a/71233703/11853111.
    private HttpServletRequest request;

    // FIXME: separate logic from UserService
    private UserService userService;

    @Before("@annotation(com.techeer.f5.jmtmonster.global.aop.annotation.AuthOnly)")
    public void checkTokenIsValid() throws ResourceNotFoundException {
        userService.findUserWithRequest(request);
    }
}

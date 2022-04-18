package com.techeer.f5.jmtmonster.security.interceptor;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.security.extractor.AuthorizationExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

// Original Code from https://ocblog.tistory.com/56
@Component
@RequiredArgsConstructor
@Slf4j
public class BearerTokenInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authExtractor;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        log.debug(">>> call BearerTokenInterceptor.preHandle");

        String token = authExtractor.extract(request, "Bearer");
        if (ObjectUtils.isEmpty(token)) {
            return true;
        }

        Optional<User> optionalUser = userRepository.findById(UUID.fromString(token));

        optionalUser.ifPresent((user) -> {
            // UUID Type
            request.setAttribute("userId", user.getId());
        });

        return true;
    }
}
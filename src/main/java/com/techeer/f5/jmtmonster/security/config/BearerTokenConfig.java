package com.techeer.f5.jmtmonster.security.config;

import com.techeer.f5.jmtmonster.security.interceptor.BearerTokenInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Original Code from https://ocblog.tistory.com/56
@Configuration
@RequiredArgsConstructor
@Slf4j
public class BearerTokenConfig implements WebMvcConfigurer {
    private final BearerTokenInterceptor bearerTokenInterceptor;

    public void addInterceptors(InterceptorRegistry registry){
        log.info(">>> BearerTokenConfig Registered.");
        registry.addInterceptor(bearerTokenInterceptor)
                .addPathPatterns("**/*")
                .excludePathPatterns("/auth/kakao/*", "/auth/verify/*");
    }
}
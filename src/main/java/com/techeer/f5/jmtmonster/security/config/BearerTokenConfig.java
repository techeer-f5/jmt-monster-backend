package com.techeer.f5.jmtmonster.security.config;

import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.security.extractor.AuthorizationExtractor;
import com.techeer.f5.jmtmonster.security.interceptor.BearerTokenInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;

// Original Code from https://ocblog.tistory.com/56
@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class BearerTokenConfig implements WebMvcConfigurer {
    private final AuthorizationExtractor authorizationExtractor;
    private final PersistentTokenRepository persistentTokenRepository;

    @Bean
    public MappedInterceptor bearerTokenInterceptor()
    {
        return new MappedInterceptor(null, new BearerTokenInterceptor(authorizationExtractor, persistentTokenRepository));
    }
}

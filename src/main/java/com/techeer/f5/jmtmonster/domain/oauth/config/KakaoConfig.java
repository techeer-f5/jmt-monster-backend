package com.techeer.f5.jmtmonster.domain.oauth.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Getter
public class KakaoConfig {
    private final String restApiKey;
    private final String frontEndCallbackUrl;
    private final String backendCallbackUrl;
    // server base url (hostname). doesn't have trailing slash.
    private final String baseUrl;

    private final String redirectUrl;
    private final String serverLoginUrl;
    private final String myInfoUrl;

    @Autowired
    public KakaoConfig(@Value("${oauth.kakao.rest-api-key}") String restApiKey,
                       @Value("${oauth.kakao.front-end-callback-url}") String frontEndCallbackUrl,
                       @Value("${oauth.kakao.back-end-callback-url}") String backendCallbackUrl,
                       @Value("${oauth.kakao.base-url}") String baseUrl) {

        this.restApiKey = restApiKey;
        this.frontEndCallbackUrl = frontEndCallbackUrl;
        this.backendCallbackUrl = backendCallbackUrl;
        this.baseUrl = baseUrl;

        this.redirectUrl = String.format("https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", restApiKey, frontEndCallbackUrl);
        this.serverLoginUrl = String.format("%s/auth/kakao/login", baseUrl);
        this.myInfoUrl = "https://kapi.kakao.com/v2/user/me";
    }

    public String getTokenUrl(@NotNull String code) {
        return String.format("https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s", restApiKey, frontEndCallbackUrl, code);
    }


}
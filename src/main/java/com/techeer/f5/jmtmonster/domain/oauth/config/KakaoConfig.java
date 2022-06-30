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
    private final String backendBaseUrl;
    private final String frontEndBaseUrl;

    private final String redirectUrl;
    private final String serverLoginUrl;
    private final String myInfoUrl;

    @Autowired
    public KakaoConfig(@Value("${oauth.kakao.rest-api-key}") String restApiKey,
                       @Value("${oauth.back-end-base-url}") String backendBaseUrl,
                       @Value("${oauth.front-end-base-url}") String frontEndBaseUrl) {

        this.restApiKey = restApiKey;
        this.backendBaseUrl = backendBaseUrl;
        this.frontEndBaseUrl = frontEndBaseUrl;
        this.backendCallbackUrl = String.format("%s/auth/kakao/callback", backendBaseUrl);
        this.frontEndCallbackUrl = String.format("%s/auth/after-sign-in/kakao", frontEndBaseUrl);

        this.redirectUrl = String.format("https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", restApiKey, frontEndCallbackUrl);
        this.serverLoginUrl = String.format("%s/auth/kakao/login", backendBaseUrl);
        this.myInfoUrl = "https://kapi.kakao.com/v2/user/me";
    }

    public String getTokenUrl(@NotNull String code) {
        return String.format("https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s", restApiKey, frontEndCallbackUrl, code);
    }


}
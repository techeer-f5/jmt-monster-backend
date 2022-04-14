package com.techeer.f5.jmtmonster.domain.oauth.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoConfig {
    private final String restApiKey;
    private final String callbackUrl;
    // server base url (hostname). doesn't have trailing slash.
    private final String baseUrl;

    private final String redirectUrl;
    private final String serverLoginUrl;
    private final String myInfoUrl;

    @Autowired
    public KakaoConfig(@Value("${oauth.kakao.rest-api-key}") String restApiKey,
                       @Value("${oauth.kakao.callback-url}") String callbackUrl,
                       @Value("${oauth.kakao.base-url}") String baseUrl) {

        this.restApiKey = restApiKey;
        this.callbackUrl = callbackUrl;
        this.baseUrl = baseUrl;

        this.redirectUrl = String.format("https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", restApiKey, callbackUrl);
        this.serverLoginUrl = String.format("%s/auth/kakao/login", baseUrl);
        this.myInfoUrl = "https://kapi.kakao.com/v2/user/me";
    }

    public String getTokenUrl(@NonNull String code) {
        return String.format("https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s", restApiKey, callbackUrl, code);
    }


}
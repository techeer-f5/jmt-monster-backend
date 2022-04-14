package com.techeer.f5.jmtmonster.domain.oauth.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoConfig {
    @Value("${oauth.kakao.rest-api-key}")
    private String restApiKey;

    @Value("${oauth.kakao.callback-url}")
    private String callbackUrl;

    @Value("${oauth.kakao.base-url}")
    // server base url (hostname). doesn't have trailing slash.
    private String baseUrl;

    private final String redirectUrl = String.format("https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", restApiKey, callbackUrl);

    private final String serverLoginUrl = String.format("%s/auth/kakao/login", baseUrl);

    private final String myInfoUrl = "https://kapi.kakao.com/v2/user/me";

    public String getTokenUrl(@NonNull String code) {
        return String.format("https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s", restApiKey, redirectUrl, code);
    }


}
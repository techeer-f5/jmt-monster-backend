package com.techeer.f5.jmtmonster.domain.oauth.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;


@Component
@Getter
public class GoogleConfig {
    private final String clientId;
    private final String clientSecret;
    private final String frontEndCallbackUrl;
    private final String backendCallbackUrl;
    private final String backendBaseUrl;
    private final String frontEndBaseUrl;
    private final String redirectUrl;
    private final String serverLoginUrl;
    @Getter(AccessLevel.NONE)
    private final String tokenRequestUrl;

    @Getter(AccessLevel.NONE)
    private final String userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";

    private final String scope = Stream.of("https://www.googleapis.com/auth/userinfo.email", "https://www.googleapis.com/auth/userinfo.profile")
                                    .reduce((a, b) -> a + " " + b)
                                    .get();

    @Autowired
    public GoogleConfig(@Value("${oauth.google.client-id}") String clientId,
                        @Value("${oauth.google.client-secret}") String clientSecret,
                        @Value("${oauth.back-end-base-url}") String backendBaseUrl,
                        @Value("${oauth.front-end-base-url}") String frontEndBaseUrl) {

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.backendBaseUrl = backendBaseUrl;
        this.frontEndBaseUrl = frontEndBaseUrl;
        this.backendCallbackUrl = String.format("%s/auth/google/callback", backendBaseUrl);
        this.frontEndCallbackUrl = String.format("%s/auth/after-sign-in/google", frontEndBaseUrl);

        this.redirectUrl = String.format("https://accounts.google.com/o/oauth2/v2/auth?client_id=%s&response_type=code&redirect_uri=%s&scope=%s", clientId, frontEndCallbackUrl, scope);
        this.serverLoginUrl = String.format("%s/auth/google/login", backendBaseUrl);
        this.tokenRequestUrl = String.format("https://oauth2.googleapis.com/token?client_id=%s&client_secret=%s&grant_type=authorization_code&redirect_uri=%s", clientId, clientSecret, frontEndCallbackUrl);
    }

    public String getUserInfoUrl(String accessToken) {
        return String.format(userInfoUrl + "&access_token=%s", accessToken);
    }

    public String getTokenRequestUrl(String code) {
        return String.format(this.tokenRequestUrl + "&code=%s", code);
    }
}

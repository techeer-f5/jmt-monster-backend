package com.techeer.f5.jmtmonster.domain.oauth.dto;

import lombok.Getter;


@Getter
// Type Signature from https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token-response
public class KakaoOAuthResponseDto {
    private String tokenType;
    private String accessToken;
    private String idToken;

    private long expiresIn;
    private String refreshToken;
    private long refreshTokenExpiresIn;

    private String scope;
}

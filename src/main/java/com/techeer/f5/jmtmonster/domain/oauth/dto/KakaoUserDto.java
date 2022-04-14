package com.techeer.f5.jmtmonster.domain.oauth.dto;

import lombok.Getter;

@Getter
public class KakaoUserDto {

    @Getter
    static class KakaoAccountDto {
        private String name;
        private String email;
    }

    private KakaoAccountDto kakaoAccount;

}

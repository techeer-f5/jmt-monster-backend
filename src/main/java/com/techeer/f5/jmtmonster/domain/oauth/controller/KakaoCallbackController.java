package com.techeer.f5.jmtmonster.domain.oauth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth/kakao/callback")
public class KakaoCallbackController {

    @Value("${oauth.kakao.rest_api_key}")
    private String restAPIKey;

    @Value("${oauth.kakao.callback_url}")
    private String callbackURL;

    @Value("${oauth.kakao.base_url}")
    private String baseURL;

    @GetMapping
    public Object callback(@RequestParam @NotBlank String code) {

    }


}

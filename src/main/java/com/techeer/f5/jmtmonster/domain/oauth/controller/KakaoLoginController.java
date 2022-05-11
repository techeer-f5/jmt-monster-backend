package com.techeer.f5.jmtmonster.domain.oauth.controller;

import com.techeer.f5.jmtmonster.domain.oauth.config.KakaoConfig;
import com.techeer.f5.jmtmonster.domain.oauth.service.KakaoCallbackService;
import com.techeer.f5.jmtmonster.domain.oauth.service.KakaoLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/auth/kakao/login")
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @GetMapping
    public void login(@NotNull HttpServletResponse response) throws IOException {
        try {
            kakaoLoginService.redirectToKakaoLogin(response);
        } catch (IOException e) {
            log.error("IOException on KakaoLoginController login /auth/kakao/login {}", e.getMessage());

            response.sendError(HttpStatus.SERVICE_UNAVAILABLE.value());
        }
    }
}

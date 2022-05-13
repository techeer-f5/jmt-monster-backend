package com.techeer.f5.jmtmonster.domain.oauth.controller;

import com.techeer.f5.jmtmonster.domain.oauth.dto.PersistentTokenDto;
import com.techeer.f5.jmtmonster.domain.oauth.service.GoogleCallbackService;
import com.techeer.f5.jmtmonster.domain.oauth.service.KakaoCallbackService;
import com.techeer.f5.jmtmonster.global.error.exception.CustomStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth/google/callback")
public class GoogleCallbackController {

    @Autowired
    private GoogleCallbackService googleCallbackService;


    @GetMapping
    public PersistentTokenDto authentication(@NotNull HttpServletResponse response, @RequestParam @NotBlank String code) {
        Optional<PersistentTokenDto> authResult = Optional.empty();

        PersistentTokenDto nullDto = PersistentTokenDto.builder().id(null).build();

        try {
            authResult = googleCallbackService.authentication(response, code);
        } catch (IOException ignored) {
            return nullDto;
        }

        if (authResult.isEmpty()) {
            return nullDto;
        }

        return PersistentTokenDto.builder().id(authResult.get().getId()).build();
    }
}

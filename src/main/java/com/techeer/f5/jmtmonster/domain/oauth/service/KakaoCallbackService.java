package com.techeer.f5.jmtmonster.domain.oauth.service;

import com.techeer.f5.jmtmonster.domain.oauth.config.KakaoConfig;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.dto.KakaoOAuthResponseDto;
import com.techeer.f5.jmtmonster.domain.oauth.dto.KakaoUserDto;
import com.techeer.f5.jmtmonster.domain.oauth.dto.PersistentTokenDto;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.oauth.domain.AuthProvider;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class KakaoCallbackService {
    @Autowired
    private KakaoConfig kakaoConfig;

    @Autowired
    @Qualifier("snakeRestTemplate")
    private RestTemplate snakeRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersistentTokenRepository tokenRepository;

    public Optional<PersistentTokenDto> authentication(@NotNull HttpServletResponse response, @NotNull String code) {
        Optional<String> optionalAccessToken = grantAccessToken(response, code);

        if (optionalAccessToken.isEmpty()) {
            return Optional.empty();
        }

        Optional<KakaoUserDto> optionalKakaoUserDto = getUser(optionalAccessToken.get());

        if (optionalKakaoUserDto.isEmpty()) {
            return Optional.empty();
        }

        KakaoUserDto kakaoUserDto = optionalKakaoUserDto.get();

        String name = kakaoUserDto.getProfileNickname();
        String email = kakaoUserDto.getEmail();

        Optional<User> userOptional = userRepository.getUserByName(name);

        User user = userOptional.orElseGet(() -> userRepository.build(name, email));

        List<PersistentToken> tokens = user.getTokens()
                .stream()
                .filter(tkn -> tkn.getProvider() == AuthProvider.KAKAO)
                .sorted((a, b) -> a.getId().compareTo(b.getId()))
                .toList();

        PersistentToken persistentToken;

        if (!tokens.isEmpty()) {
            persistentToken = tokens.get(tokens.size() - 1);
        } else {
            persistentToken = tokenRepository.build(user, AuthProvider.KAKAO);
        }

        return Optional.of(PersistentTokenDto.builder().id(persistentToken.getId()).build());
    }

    private Optional<String> grantAccessToken(@NotNull HttpServletResponse response, @NotNull String code) {
        String tokenUrl = kakaoConfig.getTokenUrl(code);

        ResponseEntity<KakaoOAuthResponseDto> entity;

        try {
            entity = snakeRestTemplate.getForEntity(tokenUrl, KakaoOAuthResponseDto.class);
        } catch (RestClientException e) {
            log.error("RestClientException on KakaoCallbackService grantAccessToken /auth/kakao/callback {}", e.getMessage());

            return Optional.empty();
        }

        int statusCode = entity.getStatusCode().value();

        Optional<KakaoOAuthResponseDto> responseBody = Optional.ofNullable(entity.getBody());

        return responseBody.map(KakaoOAuthResponseDto::getAccessToken);
    }


    private Optional<KakaoUserDto> getUser(String accessToken) {

        String myInfoUrl = kakaoConfig.getMyInfoUrl();
        ResponseEntity<KakaoUserDto> entity;

        try {
            entity = snakeRestTemplate.exchange(myInfoUrl,
                        HttpMethod.GET,
                        new HttpEntity<>(createBearerHeader(accessToken)),
                        KakaoUserDto.class);

        } catch (RestClientException e) {
            log.error("RestClientException on KakaoCallbackService getUser /auth/kakao/callback {}", e.getMessage());

            return Optional.empty();
        }

        return Optional.ofNullable(entity.getBody());
    }

    private void redirectToServerLogin(@NotNull HttpServletResponse response) throws IOException {
        String redirectUrl = kakaoConfig.getServerLoginUrl();
        response.sendRedirect(redirectUrl);
    }

    private HttpHeaders createBearerHeader(String token){
        return new HttpHeaders() {{
            set("Authorization", String.format("Bearer %s", token));
        }};
    }
}

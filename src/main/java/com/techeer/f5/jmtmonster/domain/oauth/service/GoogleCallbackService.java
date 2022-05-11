package com.techeer.f5.jmtmonster.domain.oauth.service;

import com.techeer.f5.jmtmonster.domain.oauth.config.GoogleConfig;
import com.techeer.f5.jmtmonster.domain.oauth.domain.AuthProvider;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.dto.*;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
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
public class GoogleCallbackService {
    @Autowired
    private GoogleConfig googleConfig;

    @Autowired
    @Qualifier("snakeRestTemplate")
    private RestTemplate snakeRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersistentTokenRepository tokenRepository;

    public Optional<PersistentTokenDto> authentication(@NotNull HttpServletResponse response, @NotNull String code) throws IOException {
        Optional<String> optionalAccessToken = Optional.empty();
        try {
            optionalAccessToken = grantAccessToken(response, code);
        } catch (IllegalStateException exception) {
            if (exception.getMessage().equals("invalid_request")) {
                response.sendRedirect(googleConfig.getRedirectUrl());
            }
            return Optional.empty();
        }

        if (optionalAccessToken.isEmpty()) {
            return Optional.empty();
        }

        String accessToken = optionalAccessToken.get();

        Optional<GoogleUserDto> optionalUserDto = getUser(accessToken);

        if (optionalUserDto.isEmpty()) {
            return Optional.empty();
        }

        GoogleUserDto userDto = optionalUserDto.get();

        String name = userDto.getName();
        String email = userDto.getEmail();

        Optional<User> userOptional = userRepository.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            userOptional = Optional.of(userRepository.build(name, email));
        }

        User user = userOptional.get();

        List<PersistentToken> tokens = user.getTokens()
                .stream()
                .filter(tkn -> tkn.getProvider() == AuthProvider.GOOGLE)
                .sorted((a, b) -> a.getId().compareTo(b.getId()))
                .toList();

        PersistentToken persistentToken;

        if (!tokens.isEmpty()) {
            persistentToken = tokens.get(tokens.size()-1);
        } else {
            persistentToken = tokenRepository.build(user, AuthProvider.GOOGLE);
        }

        return Optional.of(PersistentTokenDto.builder().id(persistentToken.getId()).build());
    }

    private Optional<String> grantAccessToken(@NotNull HttpServletResponse response, @NotNull String code) throws IllegalStateException {
        String tokenUrl = googleConfig.getTokenRequestUrl(code);

        ResponseEntity<GoogleOAuthResponseDto> entity;

        Map<String, String> data = new HashMap<>();

        data.put("code", code);
        data.put("client_id", googleConfig.getClientId());
        data.put("client_secret", googleConfig.getClientSecret());
        data.put("redirect_uri", googleConfig.getRedirectUrl());
        data.put("grant_type", "authorization_code");

        try {
            entity = snakeRestTemplate.postForEntity(tokenUrl, data, GoogleOAuthResponseDto.class);
        } catch (RestClientException e) {
            log.error("RestClientException on GoogleCallbackService grantAccessToken /auth/google/callback {}", e.getMessage());

            return Optional.empty();
        }

        HttpStatus statusCode = entity.getStatusCode();

        if (statusCode.isError()) {
            log.error("Error {} on GoogleCallbackService grantAccessToken /auth/google/callback", statusCode);
            return Optional.empty();
        }



        Optional<GoogleOAuthResponseDto> optionalResponseDto = Optional.ofNullable(entity.getBody());

        if (optionalResponseDto.isEmpty()) {
            log.error("Response is empty on GoogleCallbackService grantAccessToken /auth/google/callback");
            return Optional.empty();
        }

        GoogleOAuthResponseDto responseDto = optionalResponseDto.get();

        Optional<String> error = Optional.ofNullable(responseDto.getError());

        if (error.isPresent()) {
            log.error("Error response {} on GoogleCallbackService grantAccessToken /auth/google/callback", error.get());
            throw new IllegalStateException(error.get());
        }

        Optional<String> accessToken = Optional.ofNullable(responseDto.getAccessToken());

        if (accessToken.isEmpty()) {
            log.error("AccessToken is empty on GoogleCallbackService grantAccessToken /auth/google/callback");
            return Optional.empty();
        }

        return accessToken;
    }


    private Optional<GoogleUserDto> getUser(String accessToken) {

        String userInfoUrl = googleConfig.getUserInfoUrl(accessToken);
        ResponseEntity<GoogleUserDto> entity;


        try {
            entity = snakeRestTemplate.exchange(userInfoUrl,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        GoogleUserDto.class);

        } catch (RestClientException e) {
            log.error("RestClientException on GoogleCallbackService getUser /auth/kakao/callback {}", e.getMessage());

            return Optional.empty();
        }

        return Optional.ofNullable(entity.getBody());
    }

    private void redirectToServerLogin(@NotNull HttpServletResponse response) throws IOException {
        String redirectUrl = googleConfig.getServerLoginUrl();
        response.sendRedirect(redirectUrl);
    }

    private HttpHeaders createBearerHeader(String token){
        return new HttpHeaders() {{
            set("Authorization", String.format("Bearer %s", token));
        }};
    }
}

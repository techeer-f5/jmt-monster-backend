package com.techeer.f5.jmtmonster.domain.oauth.service;

import com.techeer.f5.jmtmonster.domain.oauth.config.KakaoConfig;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.dto.KakaoOAuthResponseDto;
import com.techeer.f5.jmtmonster.domain.oauth.dto.KakaoUserDto;
import com.techeer.f5.jmtmonster.domain.oauth.dto.PersistentTokenDto;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.AuthProvider;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class KakaoCallbackService {
    @Autowired
    private KakaoConfig kakaoConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersistentTokenRepository tokenRepository;

    public PersistentTokenDto authentication(@NonNull HttpServletResponse response, @NonNull String code) {
        Optional<String> accessToken = grantAccessToken(response, code);

        AtomicReference<Optional<PersistentToken>> persistentTokenResult = new AtomicReference<>(Optional.empty());

        accessToken.ifPresent((token) -> {
            Optional<KakaoUserDto> kakaoUserDto = getUser(token);

            kakaoUserDto.ifPresent((kakaoUser) -> {
                // KakaoUserDto userObj 사용해서 UserRepository 메소드로 영속화.
                // TokenRepository 사용해서 카카오용 클라이언트 - 서버용 JWT 토큰 만들고 넘겨준다.
                // Optional이 empty면? 다른 내용으로 빌더 사용..

                String name = kakaoUser.getProfileNickname();
                String email = kakaoUser.getEmail();

                Optional<User> userOptional = userRepository.getUserByName(name);

                User user = null;

                if (userOptional.isPresent()) {
                    user = userOptional.get();
                } else {
                    user = userRepository.build(name, email, AuthProvider.KAKAO);
                    userRepository.save(user);
                }

                PersistentToken persistentToken = PersistentToken.builder()
                                            .build();

                user.getTokens().add(persistentToken);

                tokenRepository.save(persistentToken);

                persistentTokenResult.set(Optional.of(persistentToken));
            });
        });

        Optional<PersistentToken> persistentToken = persistentTokenResult.get();

        if(persistentToken.isEmpty()) {
            return PersistentTokenDto.builder().id(null).build();
        }

        return persistentToken.map((token) -> {
            UUID id = token.getId();

            return PersistentTokenDto.builder().id(id).build();
        }).get();
    }

    private Optional<String> grantAccessToken(@NonNull HttpServletResponse response, @NonNull String code) {
        String tokenUrl = kakaoConfig.getTokenUrl(code);

        ResponseEntity<KakaoOAuthResponseDto> entity;

        try {
            entity = restTemplate.getForEntity(tokenUrl, KakaoOAuthResponseDto.class);
        } catch (RestClientException e) {
            log.error("RestClientException on KakaoCallbackService grantAccessToken /auth/kakao/callback {}", e.getMessage());

            try {
                redirectToServerLogin(response);
            } catch (IOException ignored) { }

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
            entity = restTemplate.exchange(myInfoUrl,
                        HttpMethod.GET,
                        new HttpEntity<>(createBearerHeader(accessToken)),
                        KakaoUserDto.class);

        } catch (RestClientException e) {
            log.error("RestClientException on KakaoCallbackService getUser /auth/kakao/callback {}", e.getMessage());

            return Optional.empty();
        }

        return Optional.ofNullable(entity.getBody());
    }

    private void redirectToServerLogin(@NonNull HttpServletResponse response) throws IOException {
        String redirectUrl = kakaoConfig.getServerLoginUrl();
        response.sendRedirect(redirectUrl);
    }

    private HttpHeaders createBearerHeader(String token){
        return new HttpHeaders() {{
            set("Authorization", String.format("Bearer %s", token));
        }};
    }
}

package com.techeer.f5.jmtmonster.domain.oauth.service;

import com.techeer.f5.jmtmonster.domain.oauth.config.KakaoConfig;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.dto.KakaoOAuthResponseDto;
import com.techeer.f5.jmtmonster.domain.oauth.dto.KakaoUserDto;
import com.techeer.f5.jmtmonster.domain.oauth.dto.PersistentTokenDto;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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

                var kakaoAccount = kakaoUser.getKakaoAccount();

                String name = kakaoAccount.getName();
                String email = kakaoAccount.getEmail();

                User user = userRepository.createAndSave(name, email);

                PersistentToken persistentToken = PersistentToken.builder()
                                            .user(user)
                                            .build();

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
            try {
                redirectToServerLogin(response);
            } catch (IOException ignored) { }

            return Optional.empty();
        }

        int statusCode = entity.getStatusCode().value();

        assert (200 <= statusCode && statusCode < 300);

        Optional<KakaoOAuthResponseDto> responseBody = Optional.ofNullable(entity.getBody());

        return responseBody.map(KakaoOAuthResponseDto::getAccessToken);
    }


    private Optional<KakaoUserDto> getUser(String accessToken) {

        String myInfoUrl = kakaoConfig.getMyInfoUrl();
        ResponseEntity<KakaoUserDto> entity;

        try {
            entity = restTemplate.getForEntity(myInfoUrl, KakaoUserDto.class);
        } catch (RestClientException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(entity.getBody());
    }

    private void redirectToServerLogin(@NonNull HttpServletResponse response) throws IOException {
        String redirectUrl = kakaoConfig.getServerLoginUrl();
        response.sendRedirect(redirectUrl);
    }
}

package com.techeer.f5.jmtmonster.domain.oauth.service;

import com.techeer.f5.jmtmonster.domain.oauth.config.KakaoConfig;
import com.techeer.f5.jmtmonster.domain.oauth.dto.KakaoOAuthResponseDto;
import com.techeer.f5.jmtmonster.domain.oauth.dto.KakaoUserDto;
import com.techeer.f5.jmtmonster.global.config.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Service
public class KakaoCallbackService {
    @Autowired
    private KakaoConfig kakaoConfig;

    @Autowired
    private RestTemplate restTemplate;

    public void authentication(@NonNull HttpServletResponse response, @NonNull String code) {
        Optional<String> accessToken = grantAccessToken(response, code);

        accessToken.ifPresent((token) -> {
            Optional<KakaoUserDto> user = getUser(token);

            user.ifPresent((userObj) -> {
                // KakaoUserDto userObj 사용해서 UserRepository 메소드로 영속화.
                // TokenRepository 사용해서 카카오용 클라이언트 - 서버용 JWT 토큰 만들고 넘겨준다.
                // Optional이 empty면? 다른 내용으로 빌더 사용..
            })
        });
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

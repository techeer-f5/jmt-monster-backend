package com.techeer.f5.jmtmonster.domain.oauth.config;

import lombok.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoConfig  {
    @Value("${oauth.kakao.REST_API_KEY}")
    private String KAKAO_REST_API_KEY;
}
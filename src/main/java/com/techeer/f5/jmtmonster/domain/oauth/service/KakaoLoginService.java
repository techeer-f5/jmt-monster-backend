package com.techeer.f5.jmtmonster.domain.oauth.service;

import com.techeer.f5.jmtmonster.domain.oauth.config.KakaoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class KakaoLoginService {
    @Autowired
    private KakaoConfig kakaoConfig;

    public void redirectToKakaoLogin(@NonNull HttpServletResponse response) throws IOException {
        String redirectUrl = kakaoConfig.getRedirectUrl();
        response.sendRedirect(redirectUrl);
    }


}

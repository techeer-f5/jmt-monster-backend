package com.techeer.f5.jmtmonster.domain.oauth.service;

import com.techeer.f5.jmtmonster.domain.oauth.config.GoogleConfig;
import com.techeer.f5.jmtmonster.domain.oauth.config.KakaoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Service
public class GoogleLoginService {
    @Autowired
    private GoogleConfig googleConfig;

    public void redirectToGoogleLogin(@NotNull HttpServletResponse response) throws IOException {
        String redirectUrl = googleConfig.getRedirectUrl();
        response.sendRedirect(redirectUrl);
    }
}

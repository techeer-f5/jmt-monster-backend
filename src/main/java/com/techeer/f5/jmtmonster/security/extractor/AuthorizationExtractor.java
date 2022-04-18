package com.techeer.f5.jmtmonster.security.extractor;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

// Original Code from https://ocblog.tistory.com/56
@Component
public class AuthorizationExtractor implements HeaderExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

    public String extract(HttpServletRequest request, String type) {
        try {
            return request.getHeader(AUTHORIZATION).substring(type.length()).trim();
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return "";
        }
    }
}
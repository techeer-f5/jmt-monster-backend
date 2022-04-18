package com.techeer.f5.jmtmonster.security.extractor;

import javax.servlet.http.HttpServletRequest;

public interface HeaderExtractor {
    String extract(HttpServletRequest request, String type);
}

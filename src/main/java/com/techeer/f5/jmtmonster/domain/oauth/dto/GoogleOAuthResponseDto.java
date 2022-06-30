package com.techeer.f5.jmtmonster.domain.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Optional;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleOAuthResponseDto {
    @Nullable
    private String error = null;
    @Nullable
    private String accessToken = null;
}

package com.techeer.f5.jmtmonster.domain.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserDto {
    private String profileNickname;
    private String email;

    @SuppressWarnings("unchecked")
    @JsonProperty("kakao_account")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private void unpackNested(Map<String,Object> kakaoAccount) {
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        this.profileNickname = (String) profile.get("nickname");
        this.email = (String) kakaoAccount.get("email");
    }

}

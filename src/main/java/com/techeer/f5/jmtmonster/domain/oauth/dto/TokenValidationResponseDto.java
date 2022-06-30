package com.techeer.f5.jmtmonster.domain.oauth.dto;

import com.techeer.f5.jmtmonster.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenValidationResponseDto {

    private Boolean isSuccess;

    @Nullable
    private UserDto user;
}

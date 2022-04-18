package com.techeer.f5.jmtmonster.domain.oauth.dto;

import com.techeer.f5.jmtmonster.domain.user.dto.UserDto;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenValidationResponseDto {
    private boolean success;

    @Nullable
    private UserDto user;
}

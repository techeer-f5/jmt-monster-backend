package com.techeer.f5.jmtmonster.domain.oauth.controller;

import com.techeer.f5.jmtmonster.domain.oauth.dto.TokenValidationResponseDto;
import com.techeer.f5.jmtmonster.domain.oauth.service.TokenValidationService;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.UserDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/validate")
public class TokenValidationController {
    private final TokenValidationService tokenValidationService;
    private final UserMapper userMapper;

    @GetMapping("/{token}")
    public ResponseEntity<TokenValidationResponseDto> validate(@PathVariable @Valid @NotNull UUID token) {
        User user = tokenValidationService.validate(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TokenValidationResponseDto.builder()
                            .isSuccess(false)
                            .user(null)
                            .build());
        }

        UserDto userDto = userMapper.toUserDto(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(TokenValidationResponseDto.builder()
                        .isSuccess(true)
                        .user(userDto)
                        .build());
    }
}

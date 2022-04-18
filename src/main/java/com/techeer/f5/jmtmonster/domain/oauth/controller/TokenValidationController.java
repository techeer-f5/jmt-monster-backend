package com.techeer.f5.jmtmonster.domain.oauth.controller;

import com.techeer.f5.jmtmonster.domain.oauth.dto.TokenValidationResponseDto;
import com.techeer.f5.jmtmonster.domain.oauth.service.TokenValidationService;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/verify")
public class TokenValidationController {
    private final TokenValidationService tokenValidationService;

    @GetMapping("/{token}")
    public ResponseEntity<TokenValidationResponseDto> validate(@PathVariable @Valid @NotNull UUID token) {
        User user = tokenValidationService.validate(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TokenValidationResponseDto.builder()
                            .success(false)
                            .user(null)
                            .build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(TokenValidationResponseDto.builder()
                        .success(true)
                        .user(user.convert())
                        .build());
    }
}

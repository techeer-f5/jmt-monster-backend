package com.techeer.f5.jmtmonster.domain.user.controller;

import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.dto.UserResponseDto;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final PersistentTokenRepository persistentTokenRepository;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyUser(HttpServletRequest request) {
        UserResponseDto emptyDto = UserResponseDto.builder()
                                    .success(false)
                                    .user(null)
                                    .build();

        ResponseEntity<UserResponseDto> emptyResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyDto);

        String userId_ = (String) request.getAttribute("userId");

        if (userId_ == null) {
            return emptyResponse;
        }

        UUID userId = UUID.fromString(userId_);


        Optional<PersistentToken> persistentToken = persistentTokenRepository.findById(userId);
        if (persistentToken.isEmpty()) {
            return emptyResponse;
        }

        UserResponseDto userResponseDto = UserResponseDto.builder()
                                                .success(true)
                                                .user(persistentToken.get()
                                                        .getUser()
                                                        .convert()).build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }
}

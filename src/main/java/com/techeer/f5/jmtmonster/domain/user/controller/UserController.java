package com.techeer.f5.jmtmonster.domain.user.controller;

import com.techeer.f5.jmtmonster.domain.oauth.dto.UserResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserDto;
import com.techeer.f5.jmtmonster.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyUser(HttpServletRequest request) {
        UserResponseDto emptyDto = UserResponseDto.builder()
                                    .success(false)
                                    .user(null)
                                    .build();

        ResponseEntity<UserResponseDto> emptyResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyDto);

        UUID userId = (UUID) request.getAttribute("userId");

        if (userId == null) {
            return emptyResponse;
        }

        Optional<UserDto> optionalUserDto = userService.findOne(userId);
        if (optionalUserDto.isEmpty()) {
            return emptyResponse;
        }

        UserResponseDto userResponseDto = UserResponseDto.builder().success(true).user(optionalUserDto.get()).build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }
}

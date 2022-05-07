package com.techeer.f5.jmtmonster.domain.user.controller;

import com.techeer.f5.jmtmonster.domain.user.dto.UserResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.ExtraUserInfoRequestDto;
import com.techeer.f5.jmtmonster.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyUser(HttpServletRequest request) {
        UserResponseDto userResponseDto = userService.getMyUser(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @PostMapping("/me/extra-info")
    public ResponseEntity<UserResponseDto> submitExtraInfo(HttpServletRequest request, @RequestBody @Valid ExtraUserInfoRequestDto extraUserInfoRequestDto) {
        UserResponseDto userResponseDto = userService.submitExtraInfo(request, extraUserInfoRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }
}

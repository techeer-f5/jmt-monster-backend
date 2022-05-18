package com.techeer.f5.jmtmonster.domain.user.controller;

import com.techeer.f5.jmtmonster.domain.user.dto.UserDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.ExtraUserInfoRequestDto;
import com.techeer.f5.jmtmonster.domain.user.service.UserService;
import com.techeer.f5.jmtmonster.global.error.exception.NotAuthorizedException;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyUser(HttpServletRequest request) {
        UserResponseDto userResponseDto = userService.getMyUser(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @PostMapping("/me/extra-info")
    @PutMapping("/me/extra-info")
    public ResponseEntity<UserResponseDto> submitExtraInfo(HttpServletRequest request, @RequestBody @Valid ExtraUserInfoRequestDto extraUserInfoRequestDto) {
        try {
            UserResponseDto userResponseDto = userService.submitExtraInfo(request, extraUserInfoRequestDto);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(userResponseDto);
        } catch (NotAuthorizedException |
                 ResourceNotFoundException |
                 RollbackException |
                 TransactionSystemException exception) {
            log.error(exception.toString());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(UserResponseDto.builder()
                        .success(false)
                        .user(UserDto.builder().id(null).build())
                        .build());
    }

}

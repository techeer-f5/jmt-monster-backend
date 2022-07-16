package com.techeer.f5.jmtmonster.domain.user.controller;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.BasicUserResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import com.techeer.f5.jmtmonster.domain.user.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<BasicUserResponseDto> detail(@PathVariable UUID userId) {
        User user = userService.findById(userId);
        BasicUserResponseDto response = userMapper.toBasicUserResponseDto(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BasicUserResponseDto>> search(
            @PageableDefault Pageable pageable, @RequestParam("email") String email) {
        Page<User> result = userService.searchByEmail(pageable, email);
        Page<BasicUserResponseDto> response = result.map(userMapper::toBasicUserResponseDto);

        return ResponseEntity.ok(response);
    }
}

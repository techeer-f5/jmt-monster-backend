package com.techeer.f5.jmtmonster.domain.home.controller;

import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoriesDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoryDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeRequestDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeResponseDto;
import com.techeer.f5.jmtmonster.domain.home.service.HomeService;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.service.UserService;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<HomeResponseDto> getHome(HttpServletRequest request) {
        User user = null;

        try {
            user = userService.findUserWithRequest(request);
        } catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body(HomeResponseDto.builder()
                                                            .name(null)
                                                            .code(null).build());
        }

        HomeResponseDto dto = homeService.findHomeByUser(user);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/history")
    public ResponseEntity<HomeHistoriesDto> getHistory(HttpServletRequest request) {
        User user = null;

        try {
            user = userService.findUserWithRequest(request);
        } catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(HomeHistoriesDto.builder().build());
        }

        HomeHistoriesDto historiesDto = homeService.getHomeHistory(user);

        return ResponseEntity.ok(historiesDto);
    }

    @PutMapping
    public ResponseEntity<HomeResponseDto> migrate(HttpServletRequest request, @Valid @RequestBody HomeRequestDto homeRequestDto) {
        User user = null;

        try {
            user = userService.findUserWithRequest(request);
        } catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(HomeResponseDto.builder()
                            .name(null)
                            .code(null).build());
        }

        HomeResponseDto homeResponseDto = homeService.migrate(user, homeRequestDto);
        return ResponseEntity.ok(homeResponseDto);
    }
}

package com.techeer.f5.jmtmonster.domain.home.controller;

import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoriesDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeRequestDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeResponseDto;
import com.techeer.f5.jmtmonster.domain.home.service.HomeService;
import com.techeer.f5.jmtmonster.domain.oauth.aop.annotation.AuthOnly;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final HomeService homeService;

    @GetMapping
    @AuthOnly
    public ResponseEntity<HomeResponseDto> getHome(HttpServletRequest request) {
        User user = userService.findUserWithRequest(request);

        HomeResponseDto dto = homeService.findHomeByUser(user);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/history")
    @AuthOnly
    public ResponseEntity<HomeHistoriesDto> getHistory(HttpServletRequest request) {
        User user = userService.findUserWithRequest(request);
        HomeHistoriesDto historiesDto = homeService.getHomeHistory(user);

        return ResponseEntity.ok(historiesDto);
    }

    @PutMapping
    @AuthOnly
    public ResponseEntity<HomeResponseDto> migrate(HttpServletRequest request, @Valid @RequestBody HomeRequestDto homeRequestDto) {
        User user = userService.findUserWithRequest(request);
        HomeResponseDto homeResponseDto = homeService.migrate(user, homeRequestDto);

        return ResponseEntity.ok(homeResponseDto);
    }
}

package com.techeer.f5.jmtmonster.domain.home.controller;

import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoriesDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeRequestDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeResponseDto;
import com.techeer.f5.jmtmonster.global.aop.annotation.AuthOnly;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @GetMapping
    @AuthOnly
    public ResponseEntity<HomeResponseDto> getHome() {
        // FIXME: implementation needed
        return null;
    }

    @GetMapping("/history")
    @AuthOnly
    public ResponseEntity<HomeHistoriesDto> getHistory() {
        // FIXME: implementation needed
        return null;
    }

    @PutMapping
    @AuthOnly
    public ResponseEntity<HomeResponseDto> migrate(@Valid @RequestBody HomeRequestDto homeRequestDto) {
        // FIXME: implementation needed
        return null;
    }
}

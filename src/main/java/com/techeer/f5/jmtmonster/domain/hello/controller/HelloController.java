package com.techeer.f5.jmtmonster.domain.hello.controller;

import com.techeer.f5.jmtmonster.domain.hello.dto.HelloRequestDto;
import com.techeer.f5.jmtmonster.domain.hello.dto.HelloResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {

    @GetMapping
    public ResponseEntity<HelloResponseDto> get() {

        HelloResponseDto response = HelloResponseDto.builder()
                .value("hello")
                .success(true)
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping
    public ResponseEntity<HelloResponseDto> create(@Validated @RequestBody HelloRequestDto dto) {

        HelloResponseDto response = HelloResponseDto.builder()
                .value("hello " + dto.getStringValue() + dto.getIntValue())
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}

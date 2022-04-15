package com.techeer.f5.jmtmonster.domain.crawling.controller;

import com.techeer.f5.jmtmonster.domain.crawling.dto.CrawlingRequestDto;
import com.techeer.f5.jmtmonster.domain.crawling.dto.CrawlingResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/crawling")
public class CrawlingController {

    @PostMapping
    public ResponseEntity<CrawlingResponseDto> getMenuByRestId(@Validated @RequestBody CrawlingRequestDto dto, @RequestParam(defaultValue = "") Long id) {

        CrawlingResponseDto response = CrawlingResponseDto.builder()
                .restId(1234L)
                .name("메뉴 이름")
                .price("메뉴 가격")
                .picture("메뉴 이미지")
                .build();

        return ResponseEntity.ok()
                .body(response);
    }
}

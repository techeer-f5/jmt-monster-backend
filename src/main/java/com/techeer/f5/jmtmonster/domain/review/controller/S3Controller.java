package com.techeer.f5.jmtmonster.domain.review.controller;


import com.techeer.f5.jmtmonster.domain.friend.dto.mapper.S3Mapper;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendResponseDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.S3ResponseDto;
import com.techeer.f5.jmtmonster.domain.review.service.S3Service;
import com.techeer.f5.jmtmonster.s3.util.S3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service service;
    private final S3Mapper mapper;

    @PostMapping
    public ResponseEntity<S3ResponseDto> uploadImage(
            @RequestBody MultipartFile image
    ) throws IOException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.toResponseDto(service.uploadImage(image)));
    }

    @GetMapping
    public ResponseEntity<S3ResponseDto> deleteImages(
            @RequestParam String filename
    ){
        return ResponseEntity
                .ok(mapper.toResponseDto(service.deleteImage(filename)));
    }
}

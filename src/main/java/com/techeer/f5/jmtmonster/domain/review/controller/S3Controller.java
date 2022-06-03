package com.techeer.f5.jmtmonster.domain.review.controller;


import com.techeer.f5.jmtmonster.domain.review.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service service;

    @PostMapping
    public String uploadImage(
            @RequestBody MultipartFile image
    ) throws IOException {
        return service.uploadImage(image);
    }

    @GetMapping
    public String deleteImages(
            @RequestParam String filename
    ){
        return service.deleteImage(filename);
    }
}

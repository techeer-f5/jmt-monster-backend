package com.techeer.f5.jmtmonster.domain.review.controller;

import com.techeer.f5.jmtmonster.domain.review.domain.Updatable;
import com.techeer.f5.jmtmonster.domain.review.service.ReviewRequestService;
import com.techeer.f5.jmtmonster.domain.review.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class S3Controller {

    private S3Service s3Service;

    @PostMapping
    public String uploadImage(MultipartFile image) throws IOException {
        return s3Service.uploadImage(image);
    }

    @DeleteMapping
    public String deleteByURL(@RequestParam String url){
        return s3Service.deleteByURL(url);
    }

    @PutMapping
    public String updateByURL(MultipartFile multipartFile, String url) throws IOException {
        return s3Service.updateByURL(multipartFile,url);
    }

}

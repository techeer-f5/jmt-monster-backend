package com.techeer.f5.jmtmonster.domain.review.controller;

import com.techeer.f5.jmtmonster.domain.review.domain.Updatable;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestModel;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ImageCreateResponseDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ImageDeleteResponseDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ImageUpdateResponseDto;
import com.techeer.f5.jmtmonster.domain.review.service.ReviewRequestService;
import com.techeer.f5.jmtmonster.domain.review.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class S3Controller {

    private S3Service s3Service;

    @PostMapping
    public ResponseEntity<ImageCreateResponseDto> uploadImage(@RequestBody MultipartFile image) throws IOException {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ImageCreateResponseDto dto = new ImageCreateResponseDto(s3Service.uploadImage(image));

        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<ImageDeleteResponseDto> deleteByURL(@RequestParam String url){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ImageDeleteResponseDto dto = new ImageDeleteResponseDto(s3Service.deleteByURL(url));

        return new ResponseEntity<>(dto, headers, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ImageUpdateResponseDto> updateByURL(
            @RequestBody MultipartFile multipartFile, @RequestParam String url)
            throws IOException {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ImageUpdateResponseDto dto = new ImageUpdateResponseDto(s3Service.deleteByURL(url));

        return new ResponseEntity<>(dto, headers, HttpStatus.OK);
    }

}

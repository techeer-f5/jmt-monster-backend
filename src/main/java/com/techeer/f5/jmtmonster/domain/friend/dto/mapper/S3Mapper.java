package com.techeer.f5.jmtmonster.domain.friend.dto.mapper;

import com.techeer.f5.jmtmonster.domain.review.dto.response.S3DeleteResponseDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.S3UploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3Mapper {

    public S3UploadResponseDto toUploadResponseDto(String url){
        return S3UploadResponseDto.builder()
                .url(url)
                .build();
    }

    public S3DeleteResponseDto toDeleteResponseDto(String filename){
        return S3DeleteResponseDto.builder()
                .filename(filename)
                .build();
    }
}

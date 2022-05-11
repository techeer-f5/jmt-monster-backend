package com.techeer.f5.jmtmonster.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FileUploadResponseDto {
    List<String> urls;
}

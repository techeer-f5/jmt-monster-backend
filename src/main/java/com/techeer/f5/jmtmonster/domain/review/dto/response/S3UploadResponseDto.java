package com.techeer.f5.jmtmonster.domain.review.dto.response;


import com.techeer.f5.jmtmonster.global.domain.dto.BaseTimeEntityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class S3UploadResponseDto extends BaseTimeEntityDto {

    @NotNull
    private String url;

}

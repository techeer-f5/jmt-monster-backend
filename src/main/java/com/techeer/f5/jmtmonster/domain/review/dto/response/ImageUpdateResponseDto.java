package com.techeer.f5.jmtmonster.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class ImageUpdateResponseDto {
    @NotNull
    String url;
}

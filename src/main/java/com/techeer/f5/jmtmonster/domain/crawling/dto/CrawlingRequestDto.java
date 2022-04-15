package com.techeer.f5.jmtmonster.domain.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrawlingRequestDto {
    @NotEmpty
    private String stringValue;

    @NotNull
    private Long intValue;
}

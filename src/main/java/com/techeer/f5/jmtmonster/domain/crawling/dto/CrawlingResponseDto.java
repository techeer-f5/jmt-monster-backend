package com.techeer.f5.jmtmonster.domain.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrawlingResponseDto {

    private Long restId;
    private String name;
    private String price;
    private String picture;
}

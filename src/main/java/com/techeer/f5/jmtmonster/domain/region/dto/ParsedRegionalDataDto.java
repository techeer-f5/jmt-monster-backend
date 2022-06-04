package com.techeer.f5.jmtmonster.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedRegionalDataDto {
    private String city;
    private String district;
    private String region;
}

package com.techeer.f5.jmtmonster.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantInfoResponseDto {

    private Long cid;
    private String name;
}

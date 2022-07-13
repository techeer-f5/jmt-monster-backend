package com.techeer.f5.jmtmonster.domain.restaurant.dto.service;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuList {
    private List<RestaurantResponseDto> menu;
}

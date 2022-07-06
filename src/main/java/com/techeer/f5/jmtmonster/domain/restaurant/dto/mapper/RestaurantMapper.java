package com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.MenuInfoResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantInfoResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.service.MenuInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {
    public Restaurant toEntity(JSONObject jsonObject, Long cid) {
        return Restaurant.builder()
                .cid(cid)
                .name(jsonObject.getAsString("name"))
                .xcoorDinate(Long.parseLong(jsonObject.getAsString("xcoorDinate")))
                .ycoorDinate(Long.parseLong(jsonObject.getAsString("ycoorDinate")))
                .build();
    }

    public RestaurantResponseDto toResponseDto(Restaurant restaurant) {
        return RestaurantResponseDto.builder()
                .cid(restaurant.getCid())
                .name(restaurant.getName())
                .build();
    }

    public MenuInfoResponseDto toMenuInfoResponseDto(MenuInfo menuInfo) {
        return MenuInfoResponseDto.builder()
                .cid(menuInfo.getCid())
                .name(menuInfo.getRestaurantName())
                .menuName(menuInfo.getMenuName())
                .build();
    }

    public RestaurantInfoResponseDto toRestaurantInfoResponseDto(MenuInfo restaurantInfo) {
        return RestaurantInfoResponseDto.builder()
                .cid(restaurantInfo.getCid())
                .name(restaurantInfo.getRestaurantName())
                .build();
    }
}

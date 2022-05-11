package com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {
    public Restaurant toEntity(JSONObject jsonObject, Long cid) {
        return Restaurant.builder()
                .cid(cid)
                .name(jsonObject.getAsString("name"))
                .x_cord(Long.parseLong(jsonObject.getAsString("x_cord")))
                .y_cord(Long.parseLong(jsonObject.getAsString("y_cord")))
                .build();
    }

    public RestaurantResponseDto toResponseDto(Restaurant restaurant) {
        return RestaurantResponseDto.builder()
                .id(restaurant.getId())
                .cid(restaurant.getCid())
                .name(restaurant.getName())
                .x_cord(restaurant.getX_cord())
                .y_cord(restaurant.getY_cord())
                .build();
    }
}

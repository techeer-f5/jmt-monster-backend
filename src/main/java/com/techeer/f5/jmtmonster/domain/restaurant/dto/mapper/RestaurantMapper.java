package com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {
    public Restaurant toEntity(Long cId, String name, Long x, Long y) {
        return Restaurant.builder()
                .cid(cId)
                .name(name)
                .x_cord(x)
                .y_cord(y)
                .build();
    }

//    public RestaurantInfo toInformation(Long cid, String restaurantName, Long x, Long y) {
//        return RestaurantInfo.builder()
//                .cId(cid)
//                .name(restaurantName)
//                .x(x)
//                .y(y)
////                .menuList()
//                .build();
//    }
    public RestaurantResponseDto toResponseDto(ArrayList menuList, Restaurant restaurant) {
        return RestaurantResponseDto.builder()
                .cid(restaurant.getCid())
                .name(restaurant.getName())
                .menuList(menuList)
                .build();
    }
}

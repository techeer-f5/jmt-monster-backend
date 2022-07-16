package com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.service.MenuList;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.service.RestaurantInfo;
import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {
    public Restaurant toEntity(Long cId, String name, Long x, Long y) {
        return Restaurant.builder()
                .cid(cId)
                .name(name)
                .xCoordinate(x)
                .yCoordinate(y)
                .build();
    }

    public RestaurantInfo toInformation(Long cid, String restaurantName, Long x, Long y, MenuList menuList) {
        return RestaurantInfo.builder()
                .cid(cid)
                .name(restaurantName)
                .xCord(x)
                .yCord(y)
                .menuList(menuList)
                .build();
    }

    public MenuList toMenuList(List menu) {
        return MenuList.builder()
                .menu(menu)
                .build();
    }

    public RestaurantResponseDto toResponseDto(RestaurantInfo restaurantInfo) {
        return RestaurantResponseDto.builder()
                .cid(restaurantInfo.getCid())
                .name(restaurantInfo.getName())
                .menuList(restaurantInfo.getMenuList())
                .build();
    }
}

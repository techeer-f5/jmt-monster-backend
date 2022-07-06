package com.techeer.f5.jmtmonster.domain.restaurant.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
//장소마다 가지고 있는 고유번호 cid와 식당이름
public class RestaurantInfo {
    private Long cid;
    private String restaurantName;
}

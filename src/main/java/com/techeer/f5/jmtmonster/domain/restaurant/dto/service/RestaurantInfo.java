package com.techeer.f5.jmtmonster.domain.restaurant.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantInfo {
    private Long cid;
    private String name;
    private Long xCord;
    private Long yCord;
    private ArrayList<String> menuList;
}

package com.techeer.f5.jmtmonster.domain.restaurant.dto.response;

import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDto {

    private Long cid;
    private String name;
    private ArrayList<String> menuList;
}

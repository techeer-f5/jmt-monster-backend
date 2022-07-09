package com.techeer.f5.jmtmonster.domain.restaurant.dto.response;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.service.MenuList;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDto {

    private Long cid;
    private String name;
    private MenuList menuList;
}

package com.techeer.f5.jmtmonster.domain.restaurant.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDto {

//    private UUID id;
    private Long cid;
    private String name;
    private ArrayList menuList;
//    private Long x_cord;
//    private Long y_cord;
}

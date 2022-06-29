package com.techeer.f5.jmtmonster.domain.restaurant.service.information;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuInfo {
    private Long cid;
    private String restaurantName;
    private List menuList;

}

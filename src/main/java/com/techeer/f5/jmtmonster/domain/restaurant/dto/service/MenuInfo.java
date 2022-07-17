package com.techeer.f5.jmtmonster.domain.restaurant.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
//장소마다 가지고 있는 고유번호 cid, 식당이름, 메뉴이름
public class MenuInfo {
    private Long cid;
    private String restaurantName;
    private String menuName;

}

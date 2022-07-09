package com.techeer.f5.jmtmonster.domain.restaurant.controller;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final MenuService menuService;

    @GetMapping("/{cid}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantInformation(@PathVariable Long cid) {
        RestaurantResponseDto restaurantInfo = menuService.getRestaurantInfo(cid);



//        요청한 cid가 기존에 존재하는 데이터일 시, HTTP status return 변경
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(menuInfoResponseDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantInfo);
    }

}

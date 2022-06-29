package com.techeer.f5.jmtmonster.domain.restaurant.controller;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.MenuInfoResponseDto;
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
    public ResponseEntity<MenuInfoResponseDto> getRestaurantInformation(@PathVariable Long cid) {
        MenuInfoResponseDto menuInfoResponseDto = menuService.getMenuByKakao(cid);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuInfoResponseDto);
    }

}

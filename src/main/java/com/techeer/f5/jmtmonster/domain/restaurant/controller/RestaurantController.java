package com.techeer.f5.jmtmonster.domain.restaurant.controller;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper.RestaurantMapper;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import com.techeer.f5.jmtmonster.domain.restaurant.repository.RestaurantRepository;
import com.techeer.f5.jmtmonster.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{cid}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantInformation(@PathVariable Long cid) {
        RestaurantResponseDto responseDto;
        if (restaurantRepository.existsByCid(cid)) {  // 정보가 있으면
            responseDto = restaurantService.getRestaurantInfo(cid);
        } else { // 정보가 없으면
            responseDto = restaurantService.saveRestaurantInfo(cid);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }
}

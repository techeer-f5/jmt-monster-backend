package com.techeer.f5.jmtmonster.domain.restaurant.controller;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper.RestaurantMapper;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import com.techeer.f5.jmtmonster.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

//    @GetMapping // Todo 해당 지역의 음식점 목록 불러오기 - 추후에 필요할 시 추가
//    public ResponseEntity<List<RestaurantResponseDto>> getList() {
//
//    }
    @PostMapping("/{cid}")
    public ResponseEntity getOneRestaurantInformation(@PathVariable Long cid) {
//    public ResponseEntity<RestaurantResponseDto> getOneRestaurantInformation(@PathVariable Long cid) {
        // Todo Cid를 통하여 kakao REST API 조회를 통한 정보 획득
        Optional<Restaurant> restaurantOptional = restaurantService.findOneByCid(cid);

        // Todo 저장된 정보가 있으면
        if (restaurantOptional.isPresent()) {  // 정보가 있으면
//            restaurantOptional.get()

            return ResponseEntity.ok()
                    .body("cid: " + cid + "\t Message: no information in db");
        }

        // Todo 저장된 정보가 없으면

        return ResponseEntity.ok()
                .body("cid: " + cid + "\t Message: no information in db");
    }
}

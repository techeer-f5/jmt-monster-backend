package com.techeer.f5.jmtmonster.domain.restaurant.service;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper.RestaurantMapper;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import com.techeer.f5.jmtmonster.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional(readOnly = true)
    public RestaurantResponseDto getRestaurantInfo(Long cid) {
        return restaurantMapper.toResponseDto(restaurantRepository.findByCid(cid).get());
    }

    @Transactional
    public RestaurantResponseDto saveRestaurantInfo(Long cid) {
        JSONObject jsonObject = new JSONObject();
        // Todo: 외부 요청을 통한 결과를 JsonObject로 반환
        // 임시 값 저장
        jsonObject.put("cid", "562795624");
        jsonObject.put("name", "롯데리아 인천공항제2여객터미널3층점");
        jsonObject.put("x_cord", "374960");
        jsonObject.put("y_cord", "1102598");

        Restaurant restaurant = restaurantMapper.toEntity(jsonObject, cid);

        return restaurantMapper.toResponseDto(restaurantRepository.save(restaurant));
    }

}

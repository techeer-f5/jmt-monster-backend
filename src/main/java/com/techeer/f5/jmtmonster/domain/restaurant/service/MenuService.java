package com.techeer.f5.jmtmonster.domain.restaurant.service;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper.RestaurantMapper;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.service.MenuList;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.service.RestaurantInfo;
import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import com.techeer.f5.jmtmonster.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;

    public RestaurantResponseDto getRestaurantInfo(@PathVariable("cid") long cid) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders); //엔티티로 만들기
        ArrayList listMenu = new ArrayList();
        String url = "https://place.map.kakao.com/main/v/";
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url + cid)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        // 전체 내용
        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
        Map<String, Object> resultMap = result.getBody();

        Map<String, Object> place = (Map<String, Object>) resultMap.get("findway");
        Map<String, Object> basicInfo = (Map<String, Object>) resultMap.get("basicInfo");
        Map<String, Object> menuInfo = (Map<String, Object>) resultMap.get("menuInfo");

        List<Map<String, Object>> menuList = (List<Map<String, Object>>) menuInfo.get("menuList");

        String restaurantName = Objects.toString(basicInfo.get("placenamefull"));
        Long xCord = Long.valueOf(String.valueOf(place.get("x")));
        Long yCord = Long.valueOf(String.valueOf(place.get("y")));

        // Get listMenu
        for (Map<String, Object> oneMenuInfo : menuList) {
            LinkedHashMap menu = (LinkedHashMap) oneMenuInfo;
            listMenu.add(menu.get("menu"));
        }

        // TODO: change raw type data
//        MenuList menuList2 = restaurantMapper.toMenuList(listMenu);
        RestaurantInfo restaurantInfo = restaurantMapper.toInformation(cid, restaurantName, xCord, yCord, listMenu);

        if (restaurantRepository.findByCid(cid).isPresent()) {
            // 이미 존재 => 조회된 정보 바로 전송
            return restaurantMapper.toResponseDto(restaurantInfo);
        }

//         db에 저장
        restaurantRepository.save(restaurantMapper.toEntity(cid, restaurantName, xCord, yCord));

        return restaurantMapper.toResponseDto(restaurantInfo);
    }

}

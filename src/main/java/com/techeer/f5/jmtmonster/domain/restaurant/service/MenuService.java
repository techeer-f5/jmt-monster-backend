package com.techeer.f5.jmtmonster.domain.restaurant.service;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper.RestaurantMapper;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.MenuInfoResponseDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MenuService {
    private String url = "https://place.map.kakao.com/main/v/";

    public MenuInfoResponseDto getMenuByKakao(@PathVariable("cidnum") long cidnum) {
        RestaurantMapper restaurantMapper = new RestaurantMapper();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url+cidnum)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        // 전체 내용
        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
        Map<String, Object> resultMap = result.getBody();
        Map<String, Object> basicInfo = (Map<String, Object>) resultMap.get("basicInfo");

        // ToPOJO
        Long cid = Long.valueOf(String.valueOf(basicInfo.get("cid")));
        String restaurantName = (String) basicInfo.get("placenamefull");
        Map<String, Object> menu = (Map<String, Object>) resultMap.get("menuInfo");
        List<Map<String, Object>> menuList = (List<Map<String, Object>>) menu.get("menuList");

        MenuInfo menuInfo = new MenuInfo(cid, restaurantName, menuList);

        return restaurantMapper.toMenuInfoResponseDto(menuInfo);
    }
}

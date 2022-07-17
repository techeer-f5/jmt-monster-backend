package com.techeer.f5.jmtmonster.domain.restaurant.service;

import com.techeer.f5.jmtmonster.domain.restaurant.dto.mapper.RestaurantMapper;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.response.RestaurantResponseDto;
import com.techeer.f5.jmtmonster.domain.restaurant.dto.service.RestaurantInfo;
import com.techeer.f5.jmtmonster.domain.restaurant.repository.RestaurantRepository;
import com.techeer.f5.jmtmonster.global.config.RestTemplateConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;
    private final RestTemplateConfig restTemplateConfig;

    public RestaurantResponseDto getRestaurantInfo(@PathVariable("cid") long cid) {
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
        ResponseEntity<Map> result = restTemplateConfig.restTemplate().exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
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
            LinkedHashMap<String, String> menu = (LinkedHashMap) oneMenuInfo;
            listMenu.add(menu.get("menu"));
        }

        // TODO: change raw type data
        RestaurantInfo restaurantInfo = restaurantMapper.toInformation(cid, restaurantName, xCord, yCord, listMenu);

        if (restaurantRepository.findByCid(cid).isPresent()) {
            // response directly when it is existed
            return restaurantMapper.toResponseDto(restaurantInfo);
        }

        restaurantRepository.save(restaurantMapper.toEntity(cid, restaurantName, xCord, yCord));

        return restaurantMapper.toResponseDto(restaurantInfo);
    }

}

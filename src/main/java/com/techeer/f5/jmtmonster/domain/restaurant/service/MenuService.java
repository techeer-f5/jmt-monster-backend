package com.techeer.f5.jmtmonster.domain.restaurant.service;

import net.minidev.json.parser.ParseException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class MenuService {
    private String url = "https://place.map.kakao.com/main/v/";

    @GetMapping("/menu/{cidnum}")
    public String callApi(@PathVariable("cidnum") long cidnum) throws UnsupportedEncodingException, ParseException {
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

        // cid 값 추출
        Map<String, Object> parsingcid = (Map<String, Object>) resultMap.get("basicInfo");
        String cid = String.valueOf(parsingcid.get("cid"));

        //menu 정보 추출
        Map<String, Object> parsingmenu = (Map<String, Object>) resultMap.get("menuInfo");
        List<Map<String, Object>> menuList = (List<Map<String, Object>>) parsingmenu.get("menuList");
        List<String> menuInfo = menuList.stream().map((e) -> (String) e.get("menu")).toList();

        //x 값 추출
        Map<String, Object> parsing_x = (Map<String, Object>) resultMap.get("findway");
        String x_code = String.valueOf(parsing_x.get("x"));

        //y 값 추출
        Map<String, Object> parsing_y = (Map<String, Object>) resultMap.get("findway");
        String y_code = String.valueOf(parsing_y.get("y"));

        return "cid 값 : " + cid + "  menu 정보 :" + menuInfo + "  x_code값 : "  +  x_code + "  y_code 값 : " + y_code;
    }
}

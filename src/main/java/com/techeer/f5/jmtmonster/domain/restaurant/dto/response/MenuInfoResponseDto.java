package com.techeer.f5.jmtmonster.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuInfoResponseDto {

    private Long cid;
    private String name;
    private List menuList;
}

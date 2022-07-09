package com.techeer.f5.jmtmonster.domain.home.mapper;

import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoriesDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoryDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeRequestDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeResponseDto;

import java.util.List;

public interface HomeMapper {
    HomeResponseDto toResponseDto(Home home);
    HomeHistoryDto toHistoryDto(Home home);
    HomeHistoriesDto toHistoriesDto(List<Home> homes);
    Home toEntity(HomeRequestDto requestDto);
    HomeRequestDto toRequestDto(Home home);
}

package com.techeer.f5.jmtmonster.domain.home.mapper;

import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoriesDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoryDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeRequestDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeResponseDto;
import com.techeer.f5.jmtmonster.domain.home.repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HomeMapperImpl implements HomeMapper {

    private final HomeRepository homeRepository;

    @Override
    public HomeResponseDto toResponseDto(Home home) {
        return HomeResponseDto.builder()
                .code(home.getCode())
                .name(home.getName())
                .build();
    }

    @Override
    public HomeHistoryDto toHistoryDto(Home home) {
        return HomeHistoryDto.builder()
                .code(home.getCode())
                .name(home.getName())
                .build();
    }

    @Override
    public HomeHistoriesDto toHistoriesDto(List<Home> homes) {
        List<HomeHistoryDto> history = homes.stream().map(this::toHistoryDto).toList();

        if (history.size() > 0) {
            history.get(0).setIsCurrentHome(true);
        }

        return HomeHistoriesDto.builder().history(history).build();
    }

    @Override
    public Home toEntity(HomeRequestDto requestDto) {
        Home home = homeRepository.findHomeByCode(requestDto.getCode());

        if (home == null) {
            home = Home.builder()
                .code(requestDto.getCode())
                .name(requestDto.getName())
                .build();
        }

        return home;
    }

    @Override
    public HomeRequestDto toRequestDto(Home home) {
        return HomeRequestDto.builder()
                .name(home.getName())
                .code(home.getCode())
                .build();
    }
}

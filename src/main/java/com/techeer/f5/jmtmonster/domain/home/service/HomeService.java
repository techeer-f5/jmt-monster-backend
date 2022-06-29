package com.techeer.f5.jmtmonster.domain.home.service;

import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoriesDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeRequestDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeResponseDto;
import com.techeer.f5.jmtmonster.domain.home.mapper.HomeMapper;
import com.techeer.f5.jmtmonster.domain.home.repository.HomeRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeRepository homeRepository;
    private final HomeMapper homeMapper;


    public HomeResponseDto findHomeByUser(User user) {
        Home home = homeRepository.findCurrentHomeByUser(user);

        if (home == null) {
            home = Home.builder()
                        .code(null)
                        .name(null)
                        .build();
        }

        return homeMapper.toResponseDto(home);
    }

    public HomeHistoriesDto getHomeHistory(User user) {
        List<Home> homes = homeRepository.findAllHomesByUser(user);
        return homeMapper.toHistoriesDto(homes);
    }

    public HomeResponseDto migrate(User user, HomeRequestDto homeRequestDto) {
        Home home = homeMapper.toEntity(homeRequestDto);
        homeRepository.migrate(user, home);

        return homeMapper.toResponseDto(home);
    }
}

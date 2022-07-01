package com.techeer.f5.jmtmonster.domain.home.mapper;


import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoriesDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoryDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeRequestDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeResponseDto;
import com.techeer.f5.jmtmonster.domain.home.repository.HomeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
// For use test db
@ActiveProfiles(profiles = {"test"})
public class HomeMapperTest {
    @Autowired
    private HomeMapper homeMapper;
    @MockBean
    private HomeRepository homeRepository;
    private Home home;
    private HomeRequestDto homeRequestDto;

    @BeforeEach
    void setUp(){
        // given

        String name = "123";
        String code = "abc";

        home = Home.builder()
                .id(UUID.randomUUID())
                .name(name)
                .code(code)
                .build();

        homeRequestDto = HomeRequestDto.builder()
                .name(name)
                .code(code)
                .build();
    }

    @Test
    void TestToResponseDto() {
        // given

        // when
        HomeResponseDto dto = homeMapper.toResponseDto(home);

        // then
        assertAll(
                () -> assertEquals(dto.getName(), home.getName()),
                () -> assertEquals(dto.getCode(), home.getCode())
        );
    }

    @Test
    void TestToHistoryDto() {
        // given

        // when
        HomeHistoryDto dto = homeMapper.toHistoryDto(home);

        // then
        assertAll(
                () -> assertEquals(dto.getName(), home.getName()),
                () -> assertEquals(dto.getCode(), home.getCode())
        );
    }

    @Test
    void TestToHistoriesDto() {
        // given
        List<Home> homes = List.of(home, home, home);

        // when
        HomeHistoriesDto historiesDto = homeMapper.toHistoriesDto(homes);

        // then
        assertEquals(historiesDto.getHistory().size(), homes.size());

        for (int i = 0; i < historiesDto.getHistory().size(); i++) {
            boolean isCurrentHomeExpect = false;

            if (i == 0) {
                isCurrentHomeExpect = true;
            }

            assertEquals(historiesDto.getHistory().get(i).getIsCurrentHome(), isCurrentHomeExpect);
        }
    }

    @Test
    void TestToEntity() {
        // given
        given(homeRepository.findHomeByCode(any())).willReturn(null);

        // when
        Home entity = homeMapper.toEntity(homeRequestDto);

        // then
        assertAll(
                () -> assertEquals(entity.getCode(), home.getCode()),
                () -> assertEquals(entity.getName(), home.getName())
        );
    }
}

package com.techeer.f5.jmtmonster.domain.home.service;

import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoriesDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeHistoryDto;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeResponseDto;
import com.techeer.f5.jmtmonster.domain.home.repository.HomeRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
// For use test db
@ActiveProfiles(profiles = {"test"})
public class HomeServiceTest {

    @MockBean
    private HomeRepository homeRepository;

    // UserMapper 클래스는 모킹이 어렵고 모킹의 이점이 없다고 생각하여 모킹하지 않음.

    @Autowired
    private HomeService homeService;

    private User user;
    private Home home;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name("이지호")
                .nickname("DPS0340")
                .email("optional.int@kakao.com")
                .address("서울대학로27번길 19-13")
                .emailVerified(true)
                .extraInfoInjected(true)
                .verified(true)
                .build();
        home = Home.builder()
                .name("abc")
                .code("123")
                .build();
    }

    @Test
    void testFindHomeByUser() {
        // given
        given(homeRepository.findCurrentHomeByUser(any())).willReturn(null);
        given(homeRepository.findCurrentHomeByUser(user)).willReturn(home);


        // when
        HomeResponseDto homeResponseDto = homeService.findHomeByUser(user);

        // then
        assertAll(
                () -> assertEquals(homeResponseDto.getCode(), home.getCode()),
                () -> assertEquals(homeResponseDto.getName(), home.getName())
        );
    }

    @Test
    void testFindHomeByUserWillFail() {
        // given
        given(homeRepository.findCurrentHomeByUser(any())).willReturn(null);
        given(homeRepository.findCurrentHomeByUser(user)).willReturn(home);

        // when
        HomeResponseDto homeResponseDto = homeService.findHomeByUser(null);

        // then
        assertNull(homeResponseDto);
    }

    @Test
    void testGetHomeHistory() {
        // given
        List<Home> givenHomes = List.of(home, home, home);

        given(homeRepository.findAllHomesByUser(any())).willReturn(null);
        given(homeRepository.findAllHomesByUser(user)).willReturn(givenHomes);


        // when
        HomeHistoriesDto homeHistoriesDto = homeService.getHomeHistory(user);

        // then
        List<HomeHistoryDto> homeHistories = homeHistoriesDto.getHistory();

        boolean firstEntry = true;

        for (var homeHistory : homeHistories) {
            boolean isCurrentHomeExpect = firstEntry;

            if (firstEntry) {
                firstEntry = false;
            }

            assertAll(
                    () -> assertEquals(homeHistory.getIsCurrentHome(), isCurrentHomeExpect),
                    () -> assertEquals(homeHistory.getName(), home.getName()),
                    () -> assertEquals(homeHistory.getCode(), home.getCode())
            );
        }
    }

    @Test
    void testGetHomeHistoryWillFail() {
        // given
        List<Home> givenHomes = List.of(home, home, home);

        given(homeRepository.findAllHomesByUser(any())).willReturn(null);
        given(homeRepository.findAllHomesByUser(user)).willReturn(givenHomes);

        // when
        HomeHistoriesDto homeHistoriesDto = homeService.getHomeHistory(null);

        // then
        assertNull(homeHistoriesDto);
    }

}

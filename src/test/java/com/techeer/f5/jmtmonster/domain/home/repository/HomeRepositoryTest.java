package com.techeer.f5.jmtmonster.domain.home.repository;

import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import com.techeer.f5.jmtmonster.domain.home.domain.HomeToUser;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
// Flush DB after each test
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// For use test db
@ActiveProfiles(profiles = {"test"})
@Slf4j
public class HomeRepositoryTest {

    @Autowired
    private HomeRepository homeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HomeToUserRepository homeToUserRepository;

    private List<Home> homes;
    private User user;

    @BeforeEach
    public void setUp() {
        // init homes

        homes = List.of(
                Home.builder()
                        .code("1")
                        .name("a")
                        .build(),
                Home.builder()
                        .code("2")
                        .name("b")
                        .build(),
                Home.builder()
                        .code("3")
                        .name("c")
                        .build()
        );

        homes = homeRepository.saveAllAndFlush(homes);

        // init user

        user = User.builder()
                .name("이지호")
                .nickname("DPS0340")
                .email("optional.int@kakao.com")
                .address("서울대학로27번길 19-13")
                .emailVerified(true)
                .extraInfoInjected(true)
                .verified(true)
                .build();

        user = userRepository.saveAndFlush(user);

        // inject sample data using migrate

        for (int i = homes.size() - 1; i >= 0; i--) {
            Home home = homes.get(i);

            homeRepository.migrate(user, home);
        }
    }

    @Test
    public void testFindAllHomeToUsersByUser() {
        // given
        Set<Home> givenHomeSet = new HashSet<>(homes);

        // when
        List<HomeToUser> homeToUsers = homeRepository.findAllHomeToUsersByUser(user);

        List<Home> homeList = homeToUsers.stream().map(HomeToUser::getHome).toList();

        Set<User> userSet = homeToUsers.stream().map(HomeToUser::getUser).collect(Collectors.toSet());
        Set<Home> homeSet = homeToUsers.stream().map(HomeToUser::getHome).collect(Collectors.toSet());

        // then
        assertAll(
                () -> assertEquals(userSet.size(), 1),
                () -> assertEquals(homeList.size(), homeSet.size())
        );

        assertAll(
                () -> assertEquals(userSet.size(), 1),
                () -> assertTrue(userSet.stream()
                        .map(User::getId).anyMatch(id -> id.equals(user.getId())))
        );

        assertAll(
                () -> assertEquals(homeSet.stream()
                                    .map(Home::getId)
                                    .collect(Collectors.toSet()),
                                givenHomeSet
                                    .stream()
                                    .map(Home::getId)
                                    .collect(Collectors.toSet()))
        );

        for (int i = 0; i < homeToUsers.size(); i++) {
            boolean isCurrentHomeExpected = false;

            if (i == 0) {
                isCurrentHomeExpected = true;
            }

            HomeToUser homeToUser = homeToUsers.get(i);

            assertEquals(homeToUser.isCurrent(), isCurrentHomeExpected);
        }
    }

    @Test
    public void testFindAllHomesByUser() {
        // given
        Set<Home> givenHomeSet = new HashSet<>(homes);

        // when
        List<Home> foundHomes = homeRepository.findAllHomesByUser(user);

        // then
        assertAll(
                () -> assertEquals(homes.size(), foundHomes.size()),
                () -> assertEquals(givenHomeSet.size(), foundHomes.size()),
                () -> assertEquals(homes
                                .stream()
                                .map(Home::getId)
                                .collect(Collectors.toSet()),
                        foundHomes
                                .stream()
                                .map(Home::getId)
                                .collect(Collectors.toSet()))
        );
    }

    @Test
    public void testFindCurrentHomeByUser() {
        // given
        Home currentHome = homes.get(0);

        // when
        Home foundHome = homeRepository.findCurrentHomeByUser(user);

        // then
        assertEquals(currentHome.getId(), foundHome.getId());
    }

    @Test
    public void testMigrate() {
        // given
        Home newHome = Home.builder()
                .code("123")
                .name("abc")
                .build();

        newHome = homeRepository.saveAndFlush(newHome);

        // when
        homeRepository.migrate(user, newHome);

        assertEquals(user.getAddress(), newHome.getName());

        List<HomeToUser> foundHomeToUsers = homeRepository.findAllHomeToUsersByUser(user);

        // then
        for (var foundHomeToUser : foundHomeToUsers) {
            if (newHome.getId().equals(foundHomeToUser.getHome().getId())) {
                assertTrue(foundHomeToUser.isCurrent());
            } else {
                assertFalse(foundHomeToUser.isCurrent());
            }
        }
    }

}

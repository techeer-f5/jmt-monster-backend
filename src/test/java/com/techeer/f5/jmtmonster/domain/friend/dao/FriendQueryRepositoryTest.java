package com.techeer.f5.jmtmonster.domain.friend.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.config.QuerydslConfig;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@DisplayName("친구 Query Repository")
@Import(value = {QuerydslConfig.class})
class FriendQueryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Nested
    @DisplayName("친구 검색")
    class SearchFriendsTest {

        private static User user1;
        private static User user2;
        private static Friend friend1;
        private static Friend friend2;

        @BeforeEach
        void setUp() {
            user1 = userRepository.save(User.builder()
                    .email("tester1@example.com")
                    .name("Tester1")
                    .nickname("Tester1")
                    .build());

            user2 = userRepository.save(User.builder()
                    .email("tester2@example.com")
                    .name("Tester2")
                    .nickname("Tester2")
                    .build());

            friend1 = friendRepository.save(Friend.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .isHangingOut(false)
                    .build());

            friend2 = friendRepository.save(Friend.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .isHangingOut(false)
                    .build());
        }

        @ParameterizedTest
        @MethodSource("paramsForSearchFriends_shouldGetResults_ok")
        @DisplayName("조건 일치 - 성공")
        void searchFriends_shouldGetResults_ok(boolean useFromUserIdCond, boolean useToUserIdCond,
                Boolean isHangingOutCond) {
            // given
            UUID fromUserId = useFromUserIdCond ? user1.getId() : null;
            UUID toUserId = useToUserIdCond ? user2.getId() : null;

            // when
            Page<Friend> result = friendRepository.searchFriends(
                    PageRequest.of(0, 10),
                    fromUserId,
                    toUserId,
                    isHangingOutCond);

            // then
            assertThat(result.stream().toList().get(0).getId()).isEqualTo(friend1.getId());
        }

        @ParameterizedTest
        @MethodSource("paramsForSearchFriends_shouldGetNoResult_ok")
        @DisplayName("조건 불일치 - 0개 조회")
        void searchFriends_shouldGetNoResult_ok(boolean useFromUserIdCond, boolean useToUserIdCond,
                Boolean isHangingOutCond) {
            // given
            UUID fromUserId = useFromUserIdCond ? user1.getId() : null;
            UUID toUserId = useToUserIdCond ? user2.getId() : null;

            // when
            Page<Friend> result = friendRepository.searchFriends(
                    PageRequest.of(0, 10),
                    fromUserId,
                    toUserId,
                    isHangingOutCond);

            // then
            assertThat(result).isEmpty();
        }

        private static Stream<Arguments> paramsForSearchFriends_shouldGetResults_ok() {
            return Stream.of(
                    Arguments.of(true, true, false),
                    Arguments.of(true, true, null),
                    Arguments.of(true, false, false),
                    Arguments.of(true, false, null),
                    Arguments.of(false, true, false),
                    Arguments.of(false, true, null),
                    Arguments.of(false, false, null),
                    Arguments.of(false, false, false)
            );
        }

        private static Stream<Arguments> paramsForSearchFriends_shouldGetNoResult_ok() {
            return Stream.of(
                    Arguments.of(true, true, true),
                    Arguments.of(true, false, true),
                    Arguments.of(false, true, true),
                    Arguments.of(false, false, true)
            );
        }
    }
}

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
import org.junit.jupiter.api.Test;
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
        private static User user3;
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

            user3 = userRepository.save(User.builder()
                    .email("tester3@example.com")
                    .name("Tester3")
                    .nickname("Tester3")
                    .build());

            friend1 = friendRepository.save(Friend.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .build());

            friend2 = friendRepository.save(Friend.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .build());
        }

        @ParameterizedTest
        @MethodSource("paramsForSearchFriends_shouldGetResults_ok")
        @DisplayName("조건 일치 - 성공")
        void searchFriends_shouldGetResults_ok(boolean useFromUserIdCond, boolean useToUserIdCond) {
            // given
            UUID fromUserId = useFromUserIdCond ? user1.getId() : null;
            UUID toUserId = useToUserIdCond ? user2.getId() : null;

            // when
            Page<Friend> result = friendRepository.searchFriends(
                    PageRequest.of(0, 10),
                    fromUserId,
                    toUserId);

            // then
            assertThat(result.stream().toList().get(0).getId()).isEqualTo(friend1.getId());
        }

        @Test
        @DisplayName("친구가 아닌 사용자가 특정 ID로 친구 조회 시도 - 0개 조회")
        void searchFriendsFromUserWithoutFriend_shouldGetNoResult_ok() {
            // given
            UUID fromUserId = user1.getId();
            UUID toUserId = user3.getId();

            // when
            Page<Friend> result = friendRepository.searchFriends(
                    PageRequest.of(0, 10),
                    fromUserId,
                    toUserId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("친구가 없는 사용자가 전체 친구 조회 시도 - 0개 조회")
        void searchFriendsFromUserWithOtherFriends_shouldGetNoResult_ok() {
            // given
            UUID fromUserId = user3.getId();

            // when
            Page<Friend> result = friendRepository.searchFriends(
                    PageRequest.of(0, 10),
                    fromUserId,
                    null);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("친구가 없는 사용자가 특정 ID로 친구 조회 시도 - 0개 조회")
        void searchFriendsWithToUserIdFromUserWithOtherFriends_shouldGetNoResult_ok() {
            // given
            UUID fromUserId = user3.getId();
            UUID toUserId = user1.getId();

            // when
            Page<Friend> result = friendRepository.searchFriends(
                    PageRequest.of(0, 10),
                    fromUserId,
                    toUserId);

            // then
            assertThat(result).isEmpty();
        }

        private static Stream<Arguments> paramsForSearchFriends_shouldGetResults_ok() {
            return Stream.of(
                    Arguments.of(true, true),
                    Arguments.of(true, false),
                    Arguments.of(true, false),
                    Arguments.of(false, true),
                    Arguments.of(false, true),
                    Arguments.of(false, false),
                    Arguments.of(false, false)
            );
        }
    }
}

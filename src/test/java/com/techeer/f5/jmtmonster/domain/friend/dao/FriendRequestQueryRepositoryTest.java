package com.techeer.f5.jmtmonster.domain.friend.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
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
@DisplayName("친구 요청 Query Repository")
@Import(value = {QuerydslConfig.class})
class FriendRequestQueryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Nested
    @DisplayName("친구 요청 검색")
    class SearchFriendsTest {

        private static User user1;
        private static User user2;
        private static FriendRequest friendRequest1;
        private static FriendRequest friendRequest2;

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

            friendRequest1 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());

            friendRequest2 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());
        }

        @ParameterizedTest
        @MethodSource("paramsForSearchFriendRequests_shouldGetResults_ok")
        @DisplayName("조건 일치 - 성공")
        void searchFriendRequests_shouldGetResults_ok(boolean useFromUserIdCond,
                boolean useToUserIdCond, FriendRequestStatus statusCond) {
            // given
            UUID fromUserId = useFromUserIdCond ? user1.getId() : null;
            UUID toUserId = useToUserIdCond ? user2.getId() : null;

            // when
            Page<FriendRequest> result = friendRequestRepository.searchFriendRequests(
                    PageRequest.of(0, 10),
                    fromUserId,
                    toUserId,
                    statusCond);

            // then
            assertThat(result.stream().toList().get(0).getId()).isEqualTo(friendRequest1.getId());
        }

        @ParameterizedTest
        @MethodSource("paramsForSearchFriendRequests_shouldGetNoResult_ok")
        @DisplayName("조건 불일치 - 성공")
        void searchFriendRequests_shouldGetNoResult_ok(boolean useFromUserIdCond,
                boolean useToUserIdCond, FriendRequestStatus statusCond) {
            // given
            UUID fromUserId = useFromUserIdCond ? user1.getId() : null;
            UUID toUserId = useToUserIdCond ? user2.getId() : null;

            // when
            Page<FriendRequest> result = friendRequestRepository.searchFriendRequests(
                    PageRequest.of(0, 10),
                    fromUserId,
                    toUserId,
                    statusCond);

            // then
            assertThat(result).isEmpty();
        }

        private static Stream<Arguments> paramsForSearchFriendRequests_shouldGetResults_ok() {
            return Stream.of(
                    Arguments.of(true, true, FriendRequestStatus.ACCEPTED),
                    Arguments.of(true, true, null),
                    Arguments.of(false, false, FriendRequestStatus.ACCEPTED),
                    Arguments.of(false, false, null),
                    Arguments.of(true, false, FriendRequestStatus.ACCEPTED),
                    Arguments.of(true, false, null),
                    Arguments.of(false, true, FriendRequestStatus.ACCEPTED),
                    Arguments.of(false, true, null)
            );
        }

        private static Stream<Arguments> paramsForSearchFriendRequests_shouldGetNoResult_ok() {
            return Stream.of(
                    Arguments.of(true, true, FriendRequestStatus.PENDING),
                    Arguments.of(false, false, FriendRequestStatus.PENDING),
                    Arguments.of(true, false, FriendRequestStatus.PENDING),
                    Arguments.of(false, true, FriendRequestStatus.PENDING)
            );
        }
    }
}

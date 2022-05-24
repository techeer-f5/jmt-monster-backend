package com.techeer.f5.jmtmonster.domain.friend.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

        @Test
        @DisplayName("전체 조건")
        void testSearchFriends_allQueries() {
            // given
            User user1 = userRepository.save(User.builder()
                    .email("tester1@example.com")
                    .name("Tester1")
                    .nickname("Tester1")
                    .build());

            User user2 = userRepository.save(User.builder()
                    .email("tester2@example.com")
                    .name("Tester2")
                    .nickname("Tester2")
                    .build());

            FriendRequest friend1 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());

            FriendRequest friend2 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());

            // when
            Page<FriendRequest> result = friendRequestRepository.searchFriendRequests(
                    PageRequest.of(0, 10),
                    user1.getId(),
                    user2.getId(),
                    FriendRequestStatus.ACCEPTED);

            // then
            System.out.println(result);
            assertThat(result.stream().toList().get(0).getId()).isEqualTo(friend1.getId());
        }

        @Test
        @DisplayName("fromUserId 제외 조건")
        void testSearchFriends_withOutFromUserId() {
            // given
            User user1 = userRepository.save(User.builder()
                    .email("tester1@example.com")
                    .name("Tester1")
                    .nickname("Tester1")
                    .build());

            User user2 = userRepository.save(User.builder()
                    .email("tester2@example.com")
                    .name("Tester2")
                    .nickname("Tester2")
                    .build());

            FriendRequest friend1 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());

            FriendRequest friend2 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());

            // when
            Page<FriendRequest> result = friendRequestRepository.searchFriendRequests(
                    PageRequest.of(0, 10),
                    null,
                    user2.getId(),
                    FriendRequestStatus.ACCEPTED);

            // then
            System.out.println(result);
            assertThat(result.stream().toList().get(0).getId()).isEqualTo(friend1.getId());
        }

        @Test
        @DisplayName("toUserId 제외 조건")
        void testSearchFriends_withOutToserId() {
            // given
            User user1 = userRepository.save(User.builder()
                    .email("tester1@example.com")
                    .name("Tester1")
                    .nickname("Tester1")
                    .build());

            User user2 = userRepository.save(User.builder()
                    .email("tester2@example.com")
                    .name("Tester2")
                    .nickname("Tester2")
                    .build());

            FriendRequest friend1 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());

            FriendRequest friend2 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());

            // when
            Page<FriendRequest> result = friendRequestRepository.searchFriendRequests(
                    PageRequest.of(0, 10),
                    user1.getId(),
                    null,
                    FriendRequestStatus.ACCEPTED);

            // then
            System.out.println(result);
            assertThat(result.stream().toList().get(0).getId()).isEqualTo(friend1.getId());
        }

        @Test
        @DisplayName("FriendRequestStatus 제외 조건")
        void testSearchFriends_withOutFriendRequestStatus() {
            // given
            User user1 = userRepository.save(User.builder()
                    .email("tester1@example.com")
                    .name("Tester1")
                    .nickname("Tester1")
                    .build());

            User user2 = userRepository.save(User.builder()
                    .email("tester2@example.com")
                    .name("Tester2")
                    .nickname("Tester2")
                    .build());

            FriendRequest friend1 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());

            FriendRequest friend2 = friendRequestRepository.save(FriendRequest.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .status(FriendRequestStatus.ACCEPTED)
                    .build());

            // when
            Page<FriendRequest> result = friendRequestRepository.searchFriendRequests(
                    PageRequest.of(0, 10),
                    user1.getId(),
                    user2.getId(),
                    null);

            // then
            System.out.println(result);
            assertThat(result.stream().toList().get(0).getId()).isEqualTo(friend1.getId());
        }
    }
}

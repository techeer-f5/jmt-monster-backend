package com.techeer.f5.jmtmonster.domain.friend.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
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

        @Test
        @DisplayName("전체 조건")
        void testSearchFriends() {
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

            Friend friend1 = friendRepository.save(Friend.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .isHangingOut(false)
                    .build());

            Friend friend2 = friendRepository.save(Friend.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .isHangingOut(false)
                    .build());

            // when
            Page<Friend> result = friendRepository.searchFriends(
                    PageRequest.of(0, 10),
                    user1.getId(),
                    user2.getId(),
                    false);

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

            Friend friend1 = friendRepository.save(Friend.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .isHangingOut(false)
                    .build());

            Friend friend2 = friendRepository.save(Friend.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .isHangingOut(false)
                    .build());

            // when
            Page<Friend> result = friendRepository.searchFriends(
                    PageRequest.of(0, 10),
                    null,
                    user2.getId(),
                    false);

            // then
            System.out.println(result);
            assertThat(result.stream().toList().get(0).getId()).isEqualTo(friend1.getId());
        }

        @Test
        @DisplayName("toUserId 제외 조건")
        void testSearchFriends_withOutToUserId() {
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

            Friend friend1 = friendRepository.save(Friend.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .isHangingOut(false)
                    .build());

            Friend friend2 = friendRepository.save(Friend.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .isHangingOut(false)
                    .build());

            // when
            Page<Friend> result = friendRepository.searchFriends(
                    PageRequest.of(0, 10),
                    user1.getId(),
                    null,
                    false);

            // then
            System.out.println(result);
            assertThat(result.stream().toList().get(0).getId()).isEqualTo(friend1.getId());
        }

        @Test
        @DisplayName("isHangingOut 제외 조건")
        void testSearchFriends_withOut() {
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

            Friend friend1 = friendRepository.save(Friend.builder()
                    .fromUser(user1)
                    .toUser(user2)
                    .isHangingOut(false)
                    .build());

            Friend friend2 = friendRepository.save(Friend.builder()
                    .fromUser(user2)
                    .toUser(user1)
                    .isHangingOut(false)
                    .build());

            // when
            Page<Friend> result = friendRepository.searchFriends(
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

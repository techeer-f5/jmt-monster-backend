package com.techeer.f5.jmtmonster.domain.friend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.techeer.f5.jmtmonster.domain.friend.dao.FriendRepository;
import com.techeer.f5.jmtmonster.domain.friend.dao.FriendRequestRepository;
import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("친구 Service")
class FriendServiceTests {

    @InjectMocks
    private FriendService friendService;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRepository friendRepository;

    @Nested
    @DisplayName("친구 요청 Service")
    class FriendRequestTests {

        @Nested
        @DisplayName("친구 요청 생성")
        class CreateFriendRequestTests {

            @Test
            @DisplayName("성공")
            void createRequest_ok() {

                // given
                FriendRequest friendRequest = FriendRequest.builder()
                        .id(UUID.randomUUID())
                        .fromUser(User.builder()
                                .id(UUID.randomUUID())
                                .name("FromUser")
                                .email("test@jmt-monster.com")
                                .build())
                        .toUser(User.builder()
                                .id(UUID.randomUUID())
                                .name("ToUser")
                                .email("test@jmt-monster.com")
                                .build())
                        .status(FriendRequestStatus.PENDING)
                        .build();

                given(friendRequestRepository.saveAndFlush(any()))
                        .willReturn(friendRequest);

                given(userRepository.findById(friendRequest.getFromUser().getId()))
                        .willReturn(Optional.of(friendRequest.getFromUser()));

                given(userRepository.findById(friendRequest.getToUser().getId()))
                        .willReturn(Optional.of(friendRequest.getToUser()));

                given(friendRequestRepository.getById(friendRequest.getId()))
                        .willReturn(friendRequest);

                // when
                friendRequest = friendService.createRequest(FriendRequestCreateServiceDto.builder()
                        .fromUserId(friendRequest.getFromUser().getId())
                        .toUserId(friendRequest.getToUser().getId())
                        .status(friendRequest.getStatus())
                        .build());

                FriendRequest actual = friendRequestRepository.getById(friendRequest.getId());

                // then
                assertThat(friendRequest.getFromUser().getId()).isEqualTo(
                        actual.getFromUser().getId());
                assertThat(friendRequest.getFromUser().getId()).isEqualTo(
                        actual.getFromUser().getId());
                assertThat(friendRequest.getToUser().getId()).isEqualTo(actual.getToUser().getId());
                assertThat(friendRequest.getStatus()).isEqualTo(actual.getStatus());
            }
        }

        @Nested
        @DisplayName("친구 요청 ID로 조회")
        class GetFriendRequestListTests {

            @Test
            @DisplayName("성공")
            void findRequestById_ok() {

                // given
                FriendRequest friendRequest = FriendRequest.builder()
                        .id(UUID.randomUUID())
                        .fromUser(User.builder()
                                .id(UUID.randomUUID())
                                .name("FromUser")
                                .email("test@jmt-monster.com")
                                .build())
                        .toUser(User.builder()
                                .id(UUID.randomUUID())
                                .name("ToUser")
                                .email("test@jmt-monster.com")
                                .build())
                        .status(FriendRequestStatus.PENDING)
                        .build();

                // when
                given(friendRequestRepository.findById(friendRequest.getId()))
                        .willReturn(Optional.of(friendRequest));

                // then
                FriendRequest actual = friendService.findRequestById(friendRequest.getId());

                assertThat(friendRequest.getFromUser().getId()).isEqualTo(
                        actual.getFromUser().getId());
                assertThat(friendRequest.getToUser().getId()).isEqualTo(actual.getToUser().getId());
                assertThat(friendRequest.getStatus()).isEqualTo(actual.getStatus());
            }
        }

        @Nested
        @DisplayName("친구 요청 수정")
        class UpdateFriendRequestTests {

            @Test
            @DisplayName("성공")
            void updateRequest_ok() {

                // given
                UUID frId = UUID.randomUUID();

                User fromUser = User.builder()
                        .id(UUID.randomUUID())
                        .name("FromUser")
                        .email("test@jmt-monster.com")
                        .build();

                User toUser = User.builder()
                        .id(UUID.randomUUID())
                        .name("ToUser")
                        .email("test@jmt-monster.com")
                        .build();

                FriendRequest existing = FriendRequest.builder()
                        .id(frId)
                        .fromUser(fromUser)
                        .toUser(toUser)
                        .status(FriendRequestStatus.PENDING)
                        .build();

                FriendRequest updated = FriendRequest.builder()
                        .id(frId)
                        .fromUser(fromUser)
                        .toUser(toUser)
                        .status(FriendRequestStatus.ACCEPTED)
                        .build();

                given(friendRequestRepository.findById(existing.getId()))
                        .willReturn(Optional.of(existing));

                given(friendRequestRepository.saveAndFlush(existing))
                        .willReturn(updated);

                given(friendRequestRepository.getById(updated.getId()))
                        .willReturn(updated);

                // when
                updated = friendService.updateRequest(existing.getId(),
                        FriendRequestUpdateServiceDto.builder()
                                .status(FriendRequestStatus.ACCEPTED)
                                .build());

                FriendRequest actual = friendRequestRepository.getById(updated.getId());

                // then
                assertThat(updated.getFromUser().getId()).isEqualTo(actual.getFromUser().getId());
                assertThat(updated.getToUser().getId()).isEqualTo(actual.getToUser().getId());
                assertThat(updated.getStatus()).isEqualTo(actual.getStatus());
            }
        }
    }

    @Nested
    @DisplayName("친구 Service")
    class FriendTests {

        @Nested
        @DisplayName("친구 ID로 조회")
        class GetFriendTests {

            @Test
            @DisplayName("성공")
            void findFriendById_ok() {

                // given
                Friend friend = Friend.builder()
                        .id(UUID.randomUUID())
                        .fromUser(User.builder()
                                .id(UUID.randomUUID())
                                .name("FromUser")
                                .email("test@jmt-monster.com")
                                .build())
                        .toUser(User.builder()
                                .id(UUID.randomUUID())
                                .name("ToUser")
                                .email("test@jmt-monster.com")
                                .build())
                        .isHangingOut(false)
                        .build();

                // when
                given(friendRepository.findById(friend.getId()))
                        .willReturn(Optional.of(friend));

                // then
                Friend actual = friendService.findFriendById(friend.getId());

                assertThat(friend.getFromUser().getId()).isEqualTo(actual.getFromUser().getId());
                assertThat(friend.getToUser().getId()).isEqualTo(actual.getToUser().getId());
                assertThat(friend.isHangingOut()).isEqualTo(actual.isHangingOut());
            }
        }

        @Nested
        @DisplayName("친구 수정")
        class UpdateFriendTests {

            @Test
            @DisplayName("성공")
            void updateFriend_ok() {

                // given
                UUID fId = UUID.randomUUID();

                User fromUser = User.builder()
                        .id(UUID.randomUUID())
                        .name("FromUser")
                        .email("test@jmt-monster.com")
                        .build();

                User toUser = User.builder()
                        .id(UUID.randomUUID())
                        .name("ToUser")
                        .email("test@jmt-monster.com")
                        .build();

                Friend existing = Friend.builder()
                        .id(fId)
                        .fromUser(fromUser)
                        .toUser(toUser)
                        .isHangingOut(false)
                        .build();

                Friend updated = Friend.builder()
                        .id(fId)
                        .fromUser(fromUser)
                        .toUser(toUser)
                        .isHangingOut(true)
                        .build();

                given(friendRepository.findById(existing.getId()))
                        .willReturn(Optional.of(existing));

                given(friendRepository.save(existing))
                        .willReturn(updated);

                given(friendRepository.getById(updated.getId()))
                        .willReturn(updated);

                // when
                updated = friendService.updateFriend(existing.getId(),
                        FriendUpdateServiceDto.builder()
                                .isHangingOut(true)
                                .build());

                Friend actual = friendRepository.getById(updated.getId());

                // then
                assertThat(updated.getFromUser().getId()).isEqualTo(actual.getFromUser().getId());
                assertThat(updated.getToUser().getId()).isEqualTo(actual.getToUser().getId());
                assertThat(updated.isHangingOut()).isEqualTo(actual.isHangingOut());
            }
        }
    }
}

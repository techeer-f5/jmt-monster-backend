package com.techeer.f5.jmtmonster.domain.friend.service;

import com.techeer.f5.jmtmonster.domain.friend.dao.FriendRepository;
import com.techeer.f5.jmtmonster.domain.friend.dao.FriendRequestRepository;
import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.error.exception.DuplicateResourceException;
import com.techeer.f5.jmtmonster.global.error.exception.FieldErrorWrapper;
import com.techeer.f5.jmtmonster.global.error.exception.InnerResourceNotFoundException;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public FriendRequest createRequest(FriendRequestCreateServiceDto dto) {

        // Check user information
        UUID fromUserId = dto.getFromUserId();
        UUID toUserId = dto.getToUserId();
        String resourceName = FriendRequest.class.getSimpleName();

        User fromUser = userRepository.findById(dto.getFromUserId())
                .orElseThrow(() -> new InnerResourceNotFoundException(
                        resourceName,
                        List.of(new FieldErrorWrapper(
                                User.class.getSimpleName(), "fromUserId",
                                fromUserId.toString(), "not found"))));
        User toUser = userRepository.findById(dto.getToUserId())
                .orElseThrow(() -> new InnerResourceNotFoundException(
                        resourceName,
                        List.of(new FieldErrorWrapper(
                                User.class.getSimpleName(), "toUserId",
                                toUserId.toString(), "not found"))));

        // Check existing condition
        if (friendRequestRepository.existsByFromUser_idAndToUser_id(fromUserId, toUserId)) {
            throw new DuplicateResourceException(resourceName,
                    List.of(new FieldErrorWrapper(resourceName, "fromUserId", fromUserId.toString(),
                                    "already exists with toUserId"),
                            new FieldErrorWrapper(resourceName, "toUserId", toUserId.toString(),
                                    "already exists with fromUserId")));
        }

        FriendRequest entity = FriendRequest.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(dto.getStatus())
                .build();

        return friendRequestRepository.saveAndFlush(entity);
    }

    @Transactional
    public FriendRequest updateRequest(UUID id, FriendRequestUpdateServiceDto dto) {
        FriendRequest entity = findRequestById(id);

        // If accepted, create a friend for both
        if (dto.getStatus() == FriendRequestStatus.ACCEPTED) {
            friendRepository.saveAndFlush(Friend.builder()
                    .fromUser(entity.getFromUser())
                    .toUser(entity.getToUser())
                    .build());
            friendRepository.saveAndFlush(Friend.builder()
                    .fromUser(entity.getToUser())
                    .toUser(entity.getFromUser())
                    .build());
        }

        entity.update(
                entity.getFromUser(),
                entity.getToUser(),
                dto.getStatus()
        );

        return friendRequestRepository.saveAndFlush(entity);
    }

    @Transactional
    public void deleteRequestById(UUID id) {
        if (!friendRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException(FriendRequest.class.getSimpleName(), "id", id);
        }
        friendRequestRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public FriendRequest findRequestById(UUID id) {
        return friendRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FriendRequest.class.getSimpleName(), "id", id));
    }

    @Transactional(readOnly = true)
    public Page<FriendRequest> findAllRequests(Pageable pageable, UUID fromUserId, UUID toUserId,
            FriendRequestStatus status) {
        return friendRequestRepository.searchFriendRequests(pageable, fromUserId, toUserId, status);
    }

    @Transactional
    public void deleteFriendById(UUID id) {
        if (!friendRepository.existsById(id)) {
            throw new ResourceNotFoundException(Friend.class.getSimpleName(), "id", id);
        }
        friendRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Friend findFriendById(UUID id) {
        return friendRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Friend.class.getSimpleName(), "id", id));
    }

    @Transactional(readOnly = true)
    public Page<Friend> findAllFriends(Pageable pageable, UUID fromUserId, UUID toUserId) {
        return friendRepository.searchFriends(pageable, fromUserId, toUserId);
    }
}

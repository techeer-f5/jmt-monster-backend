package com.techeer.f5.jmtmonster.domain.friend.service;

import com.techeer.f5.jmtmonster.domain.friend.dao.FriendRequestRepository;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
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
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public FriendRequest create(FriendRequestCreateServiceDto dto) {

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

        return friendRequestRepository.save(entity);
    }

    @Transactional
    public FriendRequest update(UUID id, FriendRequestUpdateServiceDto dto) {
        FriendRequest entity = findOneById(id);

        entity.update(
                entity.getFromUser(),
                entity.getToUser(),
                dto.getStatus()
        );
        return friendRequestRepository.save(entity);
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!friendRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException(FriendRequest.class.getSimpleName(), "id", id);
        }
        friendRequestRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public FriendRequest findOneById(UUID id) {
        return friendRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FriendRequest.class.getSimpleName(), "id", id));
    }

    @Transactional(readOnly = true)
    public Page<FriendRequest> findAll(Pageable pageable) {
        return friendRequestRepository.findAll(pageable);
    }
}

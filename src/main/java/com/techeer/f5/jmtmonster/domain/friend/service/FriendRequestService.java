package com.techeer.f5.jmtmonster.domain.friend.service;

import com.techeer.f5.jmtmonster.domain.friend.dao.FriendRequestRepository;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.global.error.exception.DuplicateResourceException;
import com.techeer.f5.jmtmonster.global.error.exception.FieldErrorWrapper;
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

    @Transactional
    public FriendRequest create(FriendRequest entity) {
        checkExistsByFromUserIdAndToUserId(entity);

        return friendRequestRepository.save(entity);
    }

    @Transactional
    public FriendRequest update(UUID id, FriendRequest updatedEntity) {
        checkExistsByFromUserIdAndToUserId(updatedEntity);

        FriendRequest existingEntity = findOneById(id);
        existingEntity.update(
                updatedEntity.getFromUser(),
                updatedEntity.getToUser(),
                updatedEntity.getStatus()
        );
        return friendRequestRepository.save(existingEntity);
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

    @Transactional(readOnly = true)
    public void checkExistsByFromUserIdAndToUserId(FriendRequest entity) {
        UUID fromUserId = entity.getFromUser().getId();
        UUID toUserId = entity.getToUser().getId();
        String resourceName = FriendRequest.class.getSimpleName();

        if (friendRequestRepository.existsByFromUser_idAndToUser_id(fromUserId, toUserId)) {
            throw new DuplicateResourceException(resourceName,
                    List.of(new FieldErrorWrapper(resourceName, "fromUserId", fromUserId.toString(),
                                    "already exists with toUserId"),
                            new FieldErrorWrapper(resourceName, "toUserId", toUserId.toString(),
                                    "already exists with fromUserId")));
        }
    }
}

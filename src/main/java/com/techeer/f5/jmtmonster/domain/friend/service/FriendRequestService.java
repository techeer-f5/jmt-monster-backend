package com.techeer.f5.jmtmonster.domain.friend.service;

import com.techeer.f5.jmtmonster.domain.friend.dao.FriendRequestRepository;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
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
        // TODO: Check duplicate
        // TODO: Check validity of users
        return friendRequestRepository.save(entity);
    }

    @Transactional
    public FriendRequest update(UUID id, FriendRequest updatedEntity) {
        // TODO: Check and get entity using ID
        // TODO: Check duplicity
        // TODO: Check validity of users
        // TODO: Check actual queries
        return friendRequestRepository.save(updatedEntity);
    }

    @Transactional
    public void deleteById(UUID id) {
        friendRequestRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public FriendRequest findOneById(UUID id) {
        return friendRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("friend-requests", "id", id));
    }

    @Transactional(readOnly = true)
    public Page<FriendRequest> findAll(Pageable pageable) {
        return friendRequestRepository.findAll(pageable);
    }
}

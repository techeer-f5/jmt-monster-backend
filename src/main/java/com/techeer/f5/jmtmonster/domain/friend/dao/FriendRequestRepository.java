package com.techeer.f5.jmtmonster.domain.friend.dao;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID>, FriendRequestCustomRepository {

    boolean existsByFromUser_idAndToUser_id(UUID fromUserId, UUID toUserId);
}

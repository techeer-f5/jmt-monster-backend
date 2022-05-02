package com.techeer.f5.jmtmonster.domain.review.dao;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRequestRepository extends JpaRepository<ReviewRequest, UUID> {
    boolean existsById(UUID uuid);
}

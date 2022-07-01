package com.techeer.f5.jmtmonster.domain.review.dao;

import com.techeer.f5.jmtmonster.domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRequestRepository extends JpaRepository<Review, UUID> {
    boolean existsById(UUID uuid);
}

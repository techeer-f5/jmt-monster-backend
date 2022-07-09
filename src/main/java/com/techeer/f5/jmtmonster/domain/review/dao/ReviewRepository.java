package com.techeer.f5.jmtmonster.domain.review.dao;

import com.techeer.f5.jmtmonster.domain.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsById(UUID uuid);
    Page<Review> findByUserId(UUID userId, Pageable pageable);
}

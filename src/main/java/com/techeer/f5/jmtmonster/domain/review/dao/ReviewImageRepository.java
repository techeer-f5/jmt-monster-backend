package com.techeer.f5.jmtmonster.domain.review.dao;

import com.techeer.f5.jmtmonster.domain.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, UUID> {
}

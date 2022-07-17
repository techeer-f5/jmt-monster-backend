package com.techeer.f5.jmtmonster.domain.review.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.home.domain.QHome;
import com.techeer.f5.jmtmonster.domain.review.domain.QReview;
import com.techeer.f5.jmtmonster.domain.review.domain.QReviewFood;
import com.techeer.f5.jmtmonster.domain.review.domain.QReviewImage;
import com.techeer.f5.jmtmonster.domain.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Review> findAllByUserId(UUID userId, Pageable pageable) {
        List<Review> result = jpaQueryFactory.selectFrom(QReview.review)
            .where(QReview.review.user.id.eq(userId))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        int total = result.size();

        return new PageImpl<>(result, pageable, total);
    }
}

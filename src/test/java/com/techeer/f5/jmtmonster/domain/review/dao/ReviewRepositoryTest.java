package com.techeer.f5.jmtmonster.domain.review.dao;

import com.techeer.f5.jmtmonster.domain.review.domain.*;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.config.QuerydslConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Review Repository")
@Import(value = {QuerydslConfig.class})
public class ReviewRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewImageRepository imageRepository;

    @Autowired
    private ReviewFoodRepository foodRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("Review ID를 이용한 검색")
    void existsByIdTest() {
        // given
        User user;
        ReviewFood food1;
        ReviewFood food2;
        List<ReviewFood> foods;
        ReviewImage image1;
        ReviewImage image2;
        List<ReviewImage> images;
        Review review;

        user = userRepository.save(User.builder()
                .email("tester1@example.com")
                .name("Tester1")
                .nickname("Tester1")
                .build());

        review = reviewRepository.save(Review.builder()
                .user(user)
                .content("Test content")
                .like(Like.LIKE)
                .star(Star.FIVE)
                .foodList(null)
                .imageList(null)
                .build());

        food1 = foodRepository.save(ReviewFood.builder()
                .review(review)
                .food("Test food 1")
                .build());
        food2 = foodRepository.save(ReviewFood.builder()
                .review(review)
                .food("Test food 2")
                .build());
        foods = List.of(food1, food2);

        image1 = imageRepository.save(ReviewImage.builder()
                .review(review)
                .url("Test URL 1")
                .build());
        image2 = imageRepository.save(ReviewImage.builder()
                .review(review)
                .url("Test URL 2")
                .build());
        images = List.of(image1, image2);

        review.setFoodList(foods);
        review.setImageList(images);

        // when
        boolean actualResult = reviewRepository.existsById(review.getId());
        boolean expectedResult = true;

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}

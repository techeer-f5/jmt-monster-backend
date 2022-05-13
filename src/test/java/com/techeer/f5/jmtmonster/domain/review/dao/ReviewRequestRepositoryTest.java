package com.techeer.f5.jmtmonster.domain.review.dao;

import com.techeer.f5.jmtmonster.domain.review.domain.*;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(profiles = {"test"})
public class ReviewRequestRepositoryTest {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewFoodRepository reviewFoodRepository;

    @Autowired
    private ReviewImageRepository reviewImageRepository;

    private User givenUser;
    private ReviewFood givenReviewFood1;
    private ReviewFood givenReviewFood2;
    private ReviewImage givenReviewImage1;
    private ReviewImage givenReviewImage2;
    private ReviewRequest givenReviewRequest;
    private UUID requestId;


    @BeforeEach
    void setUp(){
        requestId = UUID.randomUUID();

        givenUser = userRepository.save(
                User.builder()
                .id(UUID.randomUUID())
                .name("TESTOR")
                .email("TEST@EMAIL.COM")
                .build()
        );

        givenReviewFood1 = reviewFoodRepository.save(
                ReviewFood.builder()
                .id(UUID.randomUUID())
                .user(givenUser)
                .food("짜장면")
                .build()
        );

        givenReviewFood2 = reviewFoodRepository.save(
                ReviewFood.builder()
                        .id(UUID.randomUUID())
                        .user(givenUser)
                        .food("짬뽕")
                        .build()
        );

        givenReviewImage1 = reviewImageRepository.save(
                ReviewImage.builder()
                    .id(UUID.randomUUID())
                    .user(givenUser)
                    .url("given URL1")
                    .build()
        );

        givenReviewImage2 = reviewImageRepository.save(
                ReviewImage.builder()
                    .id(UUID.randomUUID())
                    .user(givenUser)
                    .url("given URL2")
                    .build()
        );

        givenReviewRequest = ReviewRequest.builder()
                .id(requestId)
                .user(givenUser)
                .content("좋아요")
                .likeStatus(Like.LIKE)
                .star(Star.FIVE)
                .foodList(List.of(givenReviewFood1,givenReviewFood2))
                .imageList(List.of(givenReviewImage1,givenReviewImage2))
                .build();
    }
    
    @Test
    @DisplayName("review request save test")
    void saveTest() {
        // given

        
        // when
        ReviewRequest savedReviewRequest  = reviewRequestRepository.save(givenReviewRequest);
        
        // then
//        assertThat(savedReviewRequest.getId()).isEqualTo(givenReviewRequest.getId());
        assertThat(savedReviewRequest.getUser()).isEqualTo(givenReviewRequest.getUser());
        assertThat(savedReviewRequest.getContent()).isEqualTo(givenReviewRequest.getContent());
        assertThat(savedReviewRequest.getLikeStatus()).isEqualTo(givenReviewRequest.getLikeStatus());
        assertThat(savedReviewRequest.getStar()).isEqualTo(givenReviewRequest.getStar());
        assertThat(savedReviewRequest.getFoodList()).isEqualTo(givenReviewRequest.getFoodList());
        assertThat(savedReviewRequest.getImageList()).isEqualTo(givenReviewRequest.getImageList());

    }

    @Test
    @DisplayName("review request existsById test")
    void existsByIdTest() {
        // given

        // when
        ReviewRequest savedReviewRequest  = reviewRequestRepository.save(givenReviewRequest);
        boolean flag1 = reviewRequestRepository.existsById(savedReviewRequest.getId());
        boolean flag2 = reviewRequestRepository.existsById(UUID.randomUUID());

        // then
        assertThat(flag1).isEqualTo(true);
        assertThat(flag2).isEqualTo(false);
    }

    
}

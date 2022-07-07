package com.techeer.f5.jmtmonster.domain.review.dao;

import com.techeer.f5.jmtmonster.domain.review.domain.*;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.config.QuerydslConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    private User givenUser;
    private ReviewFood givenFood1;
    private ReviewFood givenFood2;
    private List<ReviewFood> givenFoods1;
    private ReviewImage givenImage1;
    private ReviewImage givenImage2;
    private List<ReviewImage> givenImages1;
    private Review givenReview1;

    private ReviewFood givenFood3;
    private ReviewFood givenFood4;
    private List<ReviewFood> givenFoods2;
    private ReviewImage givenImage3;
    private ReviewImage givenImage4;
    private List<ReviewImage> givenImages2;
    private Review givenReview2;

    @BeforeEach
    void setUp() {
        givenUser = userRepository.save(User.builder()
            .email("tester1@example.com")
            .name("Tester1")
            .nickname("Tester1")
            .build());

        // Review 1
        givenReview1 = reviewRepository.save(Review.builder()
            .user(givenUser)
            .content("Test content 1")
            .like(Like.LIKE)
            .star(Star.FIVE)
            .foodList(null)
            .imageList(null)
            .build());
        givenFood1 = foodRepository.save(ReviewFood.builder()
            .review(givenReview1)
            .food("Test food 1")
            .build());
        givenFood2 = foodRepository.save(ReviewFood.builder()
            .review(givenReview1)
            .food("Test food 2")
            .build());
        givenFoods1 = List.of(givenFood1, givenFood2);
        givenImage1 = imageRepository.save(ReviewImage.builder()
            .review(givenReview1)
            .url("Test URL 1")
            .build());
        givenImage2 = imageRepository.save(ReviewImage.builder()
            .review(givenReview1)
            .url("Test URL 2")
            .build());
        givenImages1 = List.of(givenImage1, givenImage2);
        givenReview1.setFoodList(givenFoods1);
        givenReview1.setImageList(givenImages1);

        //Review 2
        givenReview2 = reviewRepository.save(Review.builder()
            .user(givenUser)
            .content("Test content 2")
            .like(Like.LIKE)
            .star(Star.FIVE)
            .foodList(null)
            .imageList(null)
            .build());
        givenFood3 = foodRepository.save(ReviewFood.builder()
            .review(givenReview2)
            .food("Test food 3")
            .build());
        givenFood4 = foodRepository.save(ReviewFood.builder()
            .review(givenReview2)
            .food("Test food 4")
            .build());
        givenFoods2 = List.of(givenFood3, givenFood4);
        givenImage3 = imageRepository.save(ReviewImage.builder()
            .review(givenReview2)
            .url("Test URL 3")
            .build());
        givenImage4 = imageRepository.save(ReviewImage.builder()
            .review(givenReview2)
            .url("Test URL 4")
            .build());
        givenImages2 = List.of(givenImage3, givenImage4);
        givenReview2.setFoodList(givenFoods2);
        givenReview2.setImageList(givenImages2);
    }

    @Nested
    @DisplayName("Review ID를 이용한 검색")
    class ExistsByIdTest {

        @Test
        @DisplayName("성공")
        void existsByIdSuccessTest() {
            // given

            // when
            boolean actualResult = reviewRepository.existsById(givenReview1.getId());
            boolean expectedResult = true;

            // then
            assertThat(actualResult).isEqualTo(expectedResult);
        }
    }

    @Nested
    @DisplayName("USER ID를 이용하여 REVIEW 리스트 검색")
    class FindByUserIdTest {

        @Test
        @DisplayName("성공")
        void FindByUserIdSuccessTest() {
            // given
            UUID userId = givenUser.getId();
            Pageable pageable = PageRequest.of(0, 10);

            // when
            Page<Review> result = reviewRepository.findAllByUserId(userId, pageable);

            // then
            assertThat(result.stream().toList().size()).isEqualTo(2);
        }

    }
}

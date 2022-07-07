package com.techeer.f5.jmtmonster.domain.review.service;

import com.techeer.f5.jmtmonster.domain.review.dao.ReviewFoodRepository;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewImageRepository;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewRepository;
import com.techeer.f5.jmtmonster.domain.review.domain.*;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class ReviewServiceTest {

    // 시간 관계상 Repository Test Code 작성하지 않아 Mocking도 하지 않음.
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewFoodRepository foodRepository;
    @Autowired
    private ReviewImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;

    private User givenUser;
    private ReviewFood givenFood1;
    private ReviewFood givenFood2;
    private List<ReviewFood> givenFoods;
    private ReviewImage givenImage1;
    private ReviewImage givenImage2;
    private List<ReviewImage> givenImages;
    private Review givenReview;

    @BeforeEach
    @Transactional
    void setUp() {
        givenUser = userRepository.save(User.builder()
            .email("tester1@example.com")
            .name("Tester1")
            .nickname("Tester1")
            .build());

        givenReview = reviewRepository.save(Review.builder()
            .user(givenUser)
            .content("Test content")
            .like(Like.LIKE)
            .star(Star.FIVE)
            .build());

        givenFood1 = foodRepository.save(ReviewFood.builder()
            .review(givenReview)
            .food("Test food 1")
            .build());
        givenFood2 = foodRepository.save(ReviewFood.builder()
            .review(givenReview)
            .food("Test food 2")
            .build());
        givenFoods = List.of(givenFood1, givenFood2);

        givenImage1 = imageRepository.save(ReviewImage.builder()
            .review(givenReview)
            .url("Test URL 1")
            .build());
        givenImage2 = imageRepository.save(ReviewImage.builder()
            .review(givenReview)
            .url("Test URL 2")
            .build());
        givenImages = List.of(givenImage1, givenImage2);

        givenReview.addFoodList(givenFoods);
        givenReview.addImageList(givenImages);
    }

    @Nested
    @DisplayName("REVIEW 생성 테스트")
    class CreateReviewTest {

        @Test
        @DisplayName("성공")
        @Transactional
        void createTest() {
            // given
            ReviewCreateServiceDto givenDto = ReviewCreateServiceDto.builder()
                .userId(givenUser.getId())
                .content("Create content")
                .like(Like.LIKE)
                .star(Star.FIVE)
                .foodList(List.of("Create food 1", "Create food 2"))
                .imageList(List.of("Create URL 1", "Create URL 2"))
                .build();

            // when
            Review actualReview = reviewService.create(givenDto);

            // then
            assertThat(actualReview.getContent()).isEqualTo("Create content");
            assertThat(actualReview.getLike()).isEqualTo(Like.LIKE);
            assertThat(actualReview.getStar()).isEqualTo(Star.FIVE);
            assertThat(actualReview.getFoodList().get(0).getFood()).isEqualTo("Create food 1");
            assertThat(actualReview.getFoodList().get(1).getFood()).isEqualTo("Create food 2");
            assertThat(actualReview.getImageList().get(0).getUrl()).isEqualTo("Create URL 1");
            assertThat(actualReview.getImageList().get(1).getUrl()).isEqualTo("Create URL 2");
        }
    }

    @Nested
    @DisplayName("REVIEW 업데이트 테스트")
    class UpdateReviewTest {

        @Test
        @DisplayName("성공")
        @Transactional
        void updateRequestSuccessTest() {
            // given
            ReviewUpdateServiceDto givenDto = ReviewUpdateServiceDto.builder()
                .reviewRequestId(givenReview.getId())
                .content("Changed content")
                .like(Like.DISLIKE)
                .star(Star.ONE)
                .foodList(List.of("new food 1", "new food 2"))
                .imageList(List.of("new url 1", "new url 2"))
                .build();

            // when
            Review actualReview = reviewService.updateRequest(givenDto);

            // then
            assertThat(actualReview.getContent()).isEqualTo("Changed content");
            assertThat(actualReview.getLike()).isEqualTo(Like.DISLIKE);
            assertThat(actualReview.getStar()).isEqualTo(Star.ONE);
            assertThat(actualReview.getFoodList().get(0).getFood()).isEqualTo("new food 1");
            assertThat(actualReview.getFoodList().get(1).getFood()).isEqualTo("new food 2");
            assertThat(actualReview.getImageList().get(0).getUrl()).isEqualTo("new url 1");
            assertThat(actualReview.getImageList().get(1).getUrl()).isEqualTo("new url 2");
        }

        @Test
        @DisplayName("실패")
        @Transactional
        void updateReviewFailTest() {
            // given
            ReviewUpdateServiceDto givenDto = ReviewUpdateServiceDto.builder()
                .reviewRequestId(UUID.randomUUID()) // 없는 review id
                .content("Changed content")
                .like(Like.DISLIKE)
                .star(Star.ONE)
                .foodList(List.of("new food 1", "new food 2"))
                .imageList(List.of("new url 1", "new url 2"))
                .build();

            // when & then
            assertThrows(ResourceNotFoundException.class, () -> {
                Review actualReview = reviewService.updateRequest(givenDto);
            });

        }
    }

    @Nested
    @DisplayName("REVIEW 검색 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("성공")
//    @Transactional
        void findByIdSuccessTest() {
            // given

            // when
            Review actualReview = reviewService.findRequestById(givenReview.getId());
            System.out.printf("GIVEN REVIEW : %s\n", givenReview);
            System.out.printf("REVIEW : %s\n", actualReview);
            // then
            assertThat(actualReview.getContent()).isEqualTo("Test content");
            assertThat(actualReview.getLike()).isEqualTo(Like.LIKE);
            assertThat(actualReview.getStar()).isEqualTo(Star.FIVE);
            assertThat(actualReview.getFoodList().get(0).getFood()).isEqualTo("Test food 1");
            assertThat(actualReview.getFoodList().get(1).getFood()).isEqualTo("Test food 2");
            assertThat(actualReview.getImageList().get(0).getUrl()).isEqualTo("Test URL 1");
            assertThat(actualReview.getImageList().get(1).getUrl()).isEqualTo("Test URL 2");
        }
    }

    @Nested
    @DisplayName("REVIEW 삭제 테스트")
    class DeleteByIdTest {

        @Test
        @DisplayName("성공")
        @Transactional
        void deleteRequestByIdSuccessTest() {
            // given

            // when
            reviewService.deleteRequestById(givenReview.getId());

            // then
            assertThrows(ResourceNotFoundException.class, () -> {
                reviewService.findRequestById(givenReview.getId());
            });
        }

        @Test
        @DisplayName("실패")
        @Transactional
        void deleteRequestByIdFailTest() {
            // given

            // when & then
            assertThrows(ResourceNotFoundException.class, () -> {
                reviewService.deleteRequestById(UUID.randomUUID());
            });
        }
    }
}

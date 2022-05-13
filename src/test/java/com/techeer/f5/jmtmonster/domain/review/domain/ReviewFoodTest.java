package com.techeer.f5.jmtmonster.domain.review.domain;

import com.techeer.f5.jmtmonster.domain.review.domain.ReviewFood;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class ReviewFoodTest {

    @Test
    @DisplayName("ReviewFood update 테스트")
    void updataTest() {
        // given
        ReviewFood givenReviewFood = ReviewFood.builder()
                .id(UUID.randomUUID())
                .user(null)
                .food("짜장면")
                .build();

        // when
        givenReviewFood.update("짬뽕");

        // then
        assertThat(givenReviewFood.getFood()).isEqualTo("짬뽕");
    }

}

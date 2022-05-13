package com.techeer.f5.jmtmonster.domain.review.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class ReviewImageTest {
    @Test
    @DisplayName("ReviewImage Domain Update 테스트")
    void ReviewImageTest() {
        // given
        ReviewImage givenReviewImage = ReviewImage.builder()
                .id(UUID.randomUUID())
                .user(null)
                .url("given URL")
                .build();

        // when
        givenReviewImage.update("Changed URL");

        // then
        assertThat(givenReviewImage.getUrl()).isEqualTo("Changed URL");
    }

}

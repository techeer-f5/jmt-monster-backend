package com.techeer.f5.jmtmonster.domain.review.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class ReviewRequestTest {
    @Test
    @DisplayName("ReviewRequest Domain Update Test")
    void ReviewRequestTest() {
        // given
        ReviewRequest givenReviewRequest = ReviewRequest.builder()
                .id(UUID.randomUUID())
                .user()
                .likeStatus()
                .foodList()
                .imageList()
                .build();

        // when

        // then
    }

}

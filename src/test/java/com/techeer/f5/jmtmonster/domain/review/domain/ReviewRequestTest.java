package com.techeer.f5.jmtmonster.domain.review.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"test"})
public class ReviewRequestTest {

    @Test
    @DisplayName("ReviewRequest Domain Update Test")
    void ReviewRequestTest() {
        // given
        ReviewRequest givenReviewRequest = ReviewRequest.builder()
                .id(UUID.randomUUID())
                .user(null)
                .content("좋아요")
                .likeStatus(Like.LIKE)
                .star(Star.FIVE)
                .foodList(null)
                .imageList(null)
                .build();

        // when
        givenReviewRequest.update("싫어요",Like.DISLIKE,Star.ONE);

        // then
        assertThat(givenReviewRequest.getContent()).isEqualTo("싫어요");
        assertThat(givenReviewRequest.getLikeStatus()).isEqualTo(Like.DISLIKE);
        assertThat(givenReviewRequest.getStar()).isEqualTo(Star.ONE);
    }

}

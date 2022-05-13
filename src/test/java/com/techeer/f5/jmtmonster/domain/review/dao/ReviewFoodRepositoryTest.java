package com.techeer.f5.jmtmonster.domain.review.dao;

import com.techeer.f5.jmtmonster.domain.review.domain.ReviewFood;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(profiles = {"test"})
public class ReviewFoodRepositoryTest {

    @Autowired
    private ReviewFoodRepository reviewFoodRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("ReviewFoodRepository save all test")
    void saveAllTest() {
        // given
        User givenUser = userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .name("TESTOR")
                .email("TEST@EMAIL.COM")
                .build());

        ReviewFood givenReviewFood1 = ReviewFood.builder()
                .id(UUID.randomUUID())
                .user(givenUser)
                .food("짜장면")
                .build();

        ReviewFood givenReviewFood2 = ReviewFood.builder()
                .id(UUID.randomUUID())
                .user(givenUser)
                .food("짬뽕")
                .build();

        List<ReviewFood> givenFoodList = List.of(givenReviewFood1,givenReviewFood2);

        // when
        List<ReviewFood> savedFoodList = reviewFoodRepository.saveAll(givenFoodList);

        // then
        assertThat(savedFoodList.size()).isEqualTo(givenFoodList.size());
    }

}

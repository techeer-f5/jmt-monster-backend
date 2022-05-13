package com.techeer.f5.jmtmonster.domain.review.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"test"})
public class ReviewImageRepositoryTest {

    @Autowired
    private ReviewImageRepository reviewImageRepository;
    
    @Test
    @DisplayName("")
    void methodTest() {
        // given
        
        // when
        
        // then
    }
    // 이 레포지토리는 사용하는 메서드가 없음.
    
}

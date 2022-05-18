package com.techeer.f5.jmtmonster.domain.review.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"test"})
public class ReviewRequestServiceTest {

    @Autowired
    ReviewRequestService reviewRequestService;

}

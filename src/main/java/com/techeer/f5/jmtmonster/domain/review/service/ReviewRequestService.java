package com.techeer.f5.jmtmonster.domain.review.service;

import com.techeer.f5.jmtmonster.domain.review.dao.ReviewRequestRepository;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewRequestService {

    private final ReviewRequestRepository reviewRequestRepository;
    private final UserRepository userRepository;



}


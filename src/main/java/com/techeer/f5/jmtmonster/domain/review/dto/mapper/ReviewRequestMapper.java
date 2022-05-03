package com.techeer.f5.jmtmonster.domain.review.dto.mapper;

import com.techeer.f5.jmtmonster.domain.review.domain.ReviewRequest;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ReviewRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewRequestMapper {

    private final UserMapper userMapper;

    public ReviewRequestResponseDto toResponseDto(ReviewRequest entity) {
        return ReviewRequestResponseDto.builder()
                .id(entity.getId())
                .user(userMapper.toBasicUserResponseDto(entity.getUser()))
                .build();
    }
}

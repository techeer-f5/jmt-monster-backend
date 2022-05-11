package com.techeer.f5.jmtmonster.domain.review.dto.mapper;

import com.techeer.f5.jmtmonster.domain.review.domain.ReviewRequest;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestUpdateServiceDto;
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
                .reviewId(entity.getId())
                .user(userMapper.toBasicUserResponseDto(entity.getUser()))
                .build();
    }

    public ReviewRequestCreateServiceDto toServiceDto(ReviewRequestCreateServiceDto dto){
        return ReviewRequestCreateServiceDto.builder()
                .like(dto.getLike())
                .content(dto.getContent())
                .star(dto.getStar())
                .foodList(dto.getFoodList())
                .imageList(dto.getImageList())
                .build();
    }

    public ReviewRequestUpdateServiceDto toServiceDto(ReviewRequestUpdateRequestDto dto){
        return ReviewRequestUpdateServiceDto.builder()
                .like(dto.getLike())
                .content(dto.getContent())
                .star(dto.getStar())
                .foodList(dto.getFoodList())
                .imageList(dto.getImageList())
                .build();
    }
}

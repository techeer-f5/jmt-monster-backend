package com.techeer.f5.jmtmonster.domain.review.dto.mapper;

import com.techeer.f5.jmtmonster.domain.review.domain.Review;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateRequestDto;
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

    public ReviewRequestResponseDto toResponseDto(Review entity) {
        return ReviewRequestResponseDto.builder()
                .id(entity.getId()) // ReviewId
                .user(userMapper.toBasicUserResponseDto(entity.getUser()))
                .build();
    }

    public ReviewRequestCreateServiceDto toServiceDto(ReviewRequestCreateRequestDto dto){
        return ReviewRequestCreateServiceDto.builder()
                .content(dto.getContent())
                .like(dto.getLike())
                .star(dto.getStar())
                .foodList(dto.getFoodList())
                .imageList(dto.getImageList())
                .build();
    }

    public ReviewRequestUpdateServiceDto toServiceDto(ReviewRequestUpdateRequestDto dto){
        return ReviewRequestUpdateServiceDto.builder()
                .reviewRequestId(dto.getReviewRequestId())
                .content(dto.getContent())
                .like(dto.getLike())
                .star(dto.getStar())
                .foodList(dto.getFoodList())
                .imageList(dto.getImageList())
                .build();
    }
}

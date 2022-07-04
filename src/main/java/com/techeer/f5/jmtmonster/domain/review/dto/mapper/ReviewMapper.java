package com.techeer.f5.jmtmonster.domain.review.dto.mapper;

import com.techeer.f5.jmtmonster.domain.review.domain.Review;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ReviewResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final UserMapper userMapper;

    public ReviewResponseDto toResponseDto(Review entity) {
        return ReviewResponseDto.builder()
                .id(entity.getId()) // ReviewId
                .user(userMapper.toBasicUserResponseDto(entity.getUser()))
                .build();
    }

    public ReviewCreateServiceDto toServiceDto(ReviewCreateRequestDto dto){
        return ReviewCreateServiceDto.builder()
                .content(dto.getContent())
                .like(dto.getLike())
                .star(dto.getStar())
                .foodList(dto.getFoodList())
                .imageList(dto.getImageList())
                .build();
    }

    public ReviewUpdateServiceDto toServiceDto(ReviewUpdateRequestDto dto){
        return ReviewUpdateServiceDto.builder()
                .reviewRequestId(dto.getReviewRequestId())
                .content(dto.getContent())
                .like(dto.getLike())
                .star(dto.getStar())
                .foodList(dto.getFoodList())
                .imageList(dto.getImageList())
                .build();
    }
}

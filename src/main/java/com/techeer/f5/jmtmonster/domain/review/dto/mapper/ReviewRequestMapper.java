package com.techeer.f5.jmtmonster.domain.review.dto.mapper;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewRequestMapper {

    public ReviewRequestCreateServiceDto toServiceDto(ReviewRequestCreateRequestDto dto) {
        return ReviewRequestCreateServiceDto.builder()
                .user(dto.getUser())
                .like(dto.getLike())
                .star(dto.getStar())
                .foodList(dto.getFoodList())
                .imageList()
                .build();
    }
//
//    public FriendRequestResponseDto toResponseDto(FriendRequest entity) {
//        return FriendRequestResponseDto.builder()
//                .id(entity.getId())
//                .fromUser(userMapper.toBasicUserResponseDto(entity.getFromUser()))
//                .toUser(userMapper.toBasicUserResponseDto(entity.getToUser()))
//                .status(entity.getStatus())
//                .build();
//    }
}

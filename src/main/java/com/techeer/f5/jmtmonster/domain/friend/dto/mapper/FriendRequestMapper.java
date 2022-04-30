package com.techeer.f5.jmtmonster.domain.friend.dto.mapper;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendRequestMapper {

    private final UserMapper userMapper;

    public FriendRequestCreateServiceDto toServiceDto(FriendRequestCreateRequestDto dto) {
        return FriendRequestCreateServiceDto.builder()
                .fromUserId(dto.getFromUserId())
                .toUserId(dto.getToUserId())
                .status(FriendRequestStatus.PENDING)
                .build();
    }

    public FriendRequestUpdateServiceDto toServiceDto(FriendRequestUpdateRequestDto dto) {
        return FriendRequestUpdateServiceDto.builder()
                .status(dto.getStatus())
                .build();
    }

    public FriendRequestResponseDto toResponseDto(FriendRequest entity) {
        return FriendRequestResponseDto.builder()
                .id(entity.getId())
                .fromUser(userMapper.toBasicUserResponseDto(entity.getFromUser()))
                .toUser(userMapper.toBasicUserResponseDto(entity.getToUser()))
                .status(entity.getStatus())
                .build();
    }
}

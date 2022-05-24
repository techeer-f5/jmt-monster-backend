package com.techeer.f5.jmtmonster.domain.friend.dto.mapper;

import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendMapper {

    private final UserMapper userMapper;

    public FriendResponseDto toResponseDto(Friend entity) {
        return FriendResponseDto.builder()
                .id(entity.getId())
                .fromUser(userMapper.toBasicUserResponseDto(entity.getFromUser()))
                .toUser(userMapper.toBasicUserResponseDto(entity.getToUser()))
                .isHangingOut(entity.isHangingOut())
                .build();
    }

    public FriendUpdateServiceDto toServiceDto(FriendUpdateRequestDto dto) {
        return FriendUpdateServiceDto.builder()
                .isHangingOut(dto.getIsHangingOut())
                .build();
    }
}

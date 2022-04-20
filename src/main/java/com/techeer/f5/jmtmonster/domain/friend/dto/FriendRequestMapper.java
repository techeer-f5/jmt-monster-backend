package com.techeer.f5.jmtmonster.domain.friend.dto;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendRequestMapper {

    // TODO: add more performant strategy than injecting repository into mapper
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public FriendRequest toEntity(FriendRequestCreateDto dto) {
        return FriendRequest.builder()
                .fromUser(userRepository.getById(dto.getFromUserId()))
                .toUser(userRepository.getById(dto.getToUserId()))
                .status(FriendRequestStatus.PENDING)
                .build();
    }

    public FriendRequest toEntity(FriendRequestUpdateRequestDto dto) {
        return FriendRequest.builder()
                .fromUser(userRepository.getById(dto.getFromUserId()))
                .toUser(userRepository.getById(dto.getToUserId()))
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

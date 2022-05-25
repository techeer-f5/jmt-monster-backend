package com.techeer.f5.jmtmonster.domain.user.dto;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public BasicUserResponseDto toBasicUserResponseDto(User entity) {
        return BasicUserResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    public UserDto toUserDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .address(entity.getAddress())
                .imageUrl(entity.getImageUrl())
                .emailVerified(entity.getEmailVerified())
                .extraInfoInjected(entity.getExtraInfoInjected())
                .verified(entity.getVerified())
                .build();
    }

    public UserResponseDto toUserResponseDto(User entity) {
        return UserResponseDto.builder()
                .isSuccess(true)
                .user(toUserDto(entity))
                .build();
    }
}

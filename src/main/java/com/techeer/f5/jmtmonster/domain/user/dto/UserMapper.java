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
}

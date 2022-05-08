package com.techeer.f5.jmtmonster.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String name;
    private String email;

    @Builder.Default
    private String nickname = null;

    @Builder.Default
    private String address = null;

    @Builder.Default
    private String imageUrl = null;

    @Builder.Default
    private Boolean emailVerified = false;

    @Builder.Default
    private Boolean extraInfoInjected = false;

    @Builder.Default
    private Boolean verified = false;
}

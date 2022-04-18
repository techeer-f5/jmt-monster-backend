package com.techeer.f5.jmtmonster.domain.user.Dto;

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
    private String nickname = null;
    private String address = null;
    private String imageUrl = null;
    private Boolean emailVerified = false;
    private Boolean extraInfoInjected = false;
    private Boolean verified = false;
}

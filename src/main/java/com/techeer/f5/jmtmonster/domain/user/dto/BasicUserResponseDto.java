package com.techeer.f5.jmtmonster.domain.user.dto;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BasicUserResponseDto {

    @NotNull
    private UUID id;

    @NotEmpty
    private String name;

    @NotBlank
    private String email;

    private String nickname;

    private String imageUrl;
}

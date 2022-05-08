package com.techeer.f5.jmtmonster.domain.user.dto;

import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ExtraUserInfoRequestDto {
    @NonNull
    @Builder.Default
    private String nickname = "";


    @NonNull
    @Builder.Default
    private String address = "";

    @Nullable
    @Builder.Default
    private String imageUrl = null;
}

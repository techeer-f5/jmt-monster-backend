package com.techeer.f5.jmtmonster.domain.review.dto.response;

import com.techeer.f5.jmtmonster.domain.user.dto.BasicUserResponseDto;
import com.techeer.f5.jmtmonster.global.domain.dto.BaseTimeEntityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponseDto extends BaseTimeEntityDto {

    @NotNull
    private UUID id; // review id

    @NotNull
    private BasicUserResponseDto user;
}

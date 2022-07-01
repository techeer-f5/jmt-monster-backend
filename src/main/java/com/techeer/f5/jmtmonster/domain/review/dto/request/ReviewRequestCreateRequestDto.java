package com.techeer.f5.jmtmonster.domain.review.dto.request;

import com.techeer.f5.jmtmonster.domain.review.domain.Like;
import com.techeer.f5.jmtmonster.domain.review.domain.Star;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestCreateRequestDto {

    @NotNull
    private UUID userId; // user 정보를 조회하여 ReviewRequest에 넣기 위함.

    @NotNull
    private String content;

    @NotNull
    private Like like;

    @NotNull
    private Star star;

    @NotNull
    private List<String> foodList;

    @NotNull
    private List<String> imageList;
    // S3 API를 이용하여 Image를 먼저 S3에 올린 후에 반환된 URL을 List 형식으로 저장함.
}

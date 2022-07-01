package com.techeer.f5.jmtmonster.domain.review.dto.request;

import com.techeer.f5.jmtmonster.domain.review.domain.Like;
import com.techeer.f5.jmtmonster.domain.review.domain.Star;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestUpdateRequestDto {
    // update는 reviewRequestId를 통해서 아예 새로운 review 생성하는 방식으로 구현함.

    @NotNull
    private UUID reviewRequestId;

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

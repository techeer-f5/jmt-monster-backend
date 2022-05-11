package com.techeer.f5.jmtmonster.domain.review.dto.request;

import com.techeer.f5.jmtmonster.domain.review.domain.Like;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewFood;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewImage;
import com.techeer.f5.jmtmonster.domain.review.domain.Star;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
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
public class ReviewRequestCreateRequestDto {

    @NotNull
    private UUID userId;

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
}

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

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestUpdateServiceDto {
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

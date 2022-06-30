package com.techeer.f5.jmtmonster.domain.review.dto.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestModel;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ReviewRequestResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
public class ReviewRequestModel extends RepresentationModel<ReviewRequestModel> {

    @JsonUnwrapped
    private final ReviewRequestResponseDto dto;
}

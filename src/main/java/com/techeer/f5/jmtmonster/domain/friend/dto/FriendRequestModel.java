package com.techeer.f5.jmtmonster.domain.friend.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
public class FriendRequestModel extends RepresentationModel<FriendRequestModel> {

    @JsonUnwrapped
    private final FriendRequestResponseDto dto;
}

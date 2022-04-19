package com.techeer.f5.jmtmonster.domain.friend.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.techeer.f5.jmtmonster.domain.friend.dto.FriendRequestResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
public class FriendRequestModel extends RepresentationModel<FriendRequestModel> {

    @JsonUnwrapped
    private final FriendRequestResponseDto dto;
}

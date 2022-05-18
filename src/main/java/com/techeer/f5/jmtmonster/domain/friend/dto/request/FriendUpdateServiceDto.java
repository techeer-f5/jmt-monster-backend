package com.techeer.f5.jmtmonster.domain.friend.dto.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendUpdateServiceDto {

    @NotNull
    @JsonProperty("isHangingOut")
    private boolean isHangingOut;
}

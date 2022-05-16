package com.techeer.f5.jmtmonster.domain.friend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techeer.f5.jmtmonster.domain.user.dto.BasicUserResponseDto;
import com.techeer.f5.jmtmonster.global.domain.dto.BaseTimeEntityDto;
import java.util.UUID;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FriendResponseDto extends BaseTimeEntityDto {

    @NotNull
    private UUID id;

    @NotNull
    private BasicUserResponseDto fromUser;

    @NotNull
    private BasicUserResponseDto toUser;

    @NotNull
    @JsonProperty("isAccepted")
    private boolean isAccepted;

    @NotNull
    @JsonProperty("isHangingOut")
    private boolean isHangingOut;
}

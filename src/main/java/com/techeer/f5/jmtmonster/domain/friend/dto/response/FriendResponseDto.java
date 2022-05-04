package com.techeer.f5.jmtmonster.domain.friend.dto.response;

import com.techeer.f5.jmtmonster.domain.user.dto.BasicUserResponseDto;
import com.techeer.f5.jmtmonster.global.domain.dto.BaseTimeEntityDto;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendResponseDto extends BaseTimeEntityDto {

    @NotNull
    private UUID id;

    @NotNull
    private BasicUserResponseDto fromUser;

    @NotNull
    private BasicUserResponseDto toUser;

    @NotNull
    private boolean isAccepted;

    @NotNull
    private boolean isHangingOut;
}

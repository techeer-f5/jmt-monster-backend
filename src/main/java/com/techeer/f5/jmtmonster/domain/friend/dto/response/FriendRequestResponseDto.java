package com.techeer.f5.jmtmonster.domain.friend.dto.response;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.user.dto.BasicUserResponseDto;
import com.techeer.f5.jmtmonster.global.domain.dto.BaseTimeEntityDto;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestResponseDto extends BaseTimeEntityDto {

    @NotNull
    private UUID id;

    @NotNull
    private BasicUserResponseDto fromUser;

    @NotNull
    private BasicUserResponseDto toUser;

    @NotNull
    private FriendRequestStatus status;
}

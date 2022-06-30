package com.techeer.f5.jmtmonster.domain.friend.dto.request;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequestUpdateRequestDto {

    @NotNull
    private FriendRequestStatus status;
}

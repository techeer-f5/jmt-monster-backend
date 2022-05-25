package com.techeer.f5.jmtmonster.domain.friend.dao;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRequestQueryRepository {

    Page<FriendRequest> searchFriendRequests(Pageable pageable, UUID fromUserIdCond,
            UUID toUserIdCond, FriendRequestStatus statusCond);
}

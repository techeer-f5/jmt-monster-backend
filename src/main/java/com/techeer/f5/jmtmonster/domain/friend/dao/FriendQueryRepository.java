package com.techeer.f5.jmtmonster.domain.friend.dao;

import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendQueryRepository {

    Page<Friend> searchFriends(Pageable pageable, UUID fromUserIdCond, UUID toUserIdCond,
            Boolean isHangingOutCond);
}

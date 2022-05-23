package com.techeer.f5.jmtmonster.domain.friend.dao;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRequestCustomRepository {

    Page<FriendRequest> findAll(Pageable pageable);
}

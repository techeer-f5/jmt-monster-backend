package com.techeer.f5.jmtmonster.domain.friend.dao;

import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, UUID>,
        FriendQueryRepository {

}

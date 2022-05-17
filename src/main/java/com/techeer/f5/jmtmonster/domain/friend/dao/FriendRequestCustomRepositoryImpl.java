package com.techeer.f5.jmtmonster.domain.friend.dao;

import static com.techeer.f5.jmtmonster.domain.friend.domain.QFriendRequest.friendRequest;
import static com.techeer.f5.jmtmonster.domain.user.domain.QUser.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendRequestCustomRepositoryImpl implements FriendRequestCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FriendRequest> findAll(Pageable pageable) {
        List<FriendRequest> query = jpaQueryFactory
                .selectFrom(friendRequest)
                .innerJoin(friendRequest.fromUser, user)
                .fetchJoin()
                .innerJoin(friendRequest.toUser, user)
                .fetchJoin()
                .fetch();

        JPAQuery<Long> countQuery = getCount();

        return PageableExecutionUtils.getPage(query, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCount() {
        return jpaQueryFactory
                .select(friendRequest.count())
                .from(friendRequest);
    }
}

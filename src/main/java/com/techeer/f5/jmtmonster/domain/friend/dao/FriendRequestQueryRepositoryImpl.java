package com.techeer.f5.jmtmonster.domain.friend.dao;

import static com.techeer.f5.jmtmonster.domain.friend.domain.QFriendRequest.friendRequest;
import static com.techeer.f5.jmtmonster.domain.user.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendRequestQueryRepositoryImpl implements FriendRequestQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FriendRequest> searchFriendRequests(Pageable pageable, UUID fromUserIdCond,
            UUID toUserIdCond, FriendRequestStatus statusCond) {
        List<FriendRequest> query = jpaQueryFactory
                .selectFrom(friendRequest)
                .innerJoin(friendRequest.fromUser, user)
                .fetchJoin()
                .innerJoin(friendRequest.toUser, user)
                .fetchJoin()
                .where(fromUserIdEq(fromUserIdCond),
                        toUserIdEq(toUserIdCond),
                        statusEq(statusCond))
                .fetch();

        JPAQuery<Long> countQuery = getCount();

        return PageableExecutionUtils.getPage(query, pageable, countQuery::fetchOne);
    }

    private BooleanExpression fromUserIdEq(UUID fromUserIdCond) {
        return fromUserIdCond != null ? friendRequest.fromUser.id.eq(fromUserIdCond) : null;
    }

    private BooleanExpression toUserIdEq(UUID toUserIdCond) {
        return toUserIdCond != null ? friendRequest.toUser.id.eq(toUserIdCond) : null;
    }

    private BooleanExpression statusEq(FriendRequestStatus statusCond) {
        return statusCond != null ? friendRequest.status.eq(statusCond) : null;
    }

    private JPAQuery<Long> getCount() {
        return jpaQueryFactory
                .select(friendRequest.count())
                .from(friendRequest);
    }
}

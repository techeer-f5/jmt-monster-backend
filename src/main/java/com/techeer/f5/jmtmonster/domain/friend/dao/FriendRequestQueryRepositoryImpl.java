package com.techeer.f5.jmtmonster.domain.friend.dao;

import static com.techeer.f5.jmtmonster.domain.friend.domain.QFriendRequest.friendRequest;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.user.domain.QUser;
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
        QUser fromUser = new QUser("fromUser");
        QUser toUser = new QUser("toUser");

        List<FriendRequest> query = jpaQueryFactory
                .selectFrom(friendRequest)
                .innerJoin(friendRequest.fromUser, fromUser)
                .fetchJoin()
                .innerJoin(friendRequest.toUser, toUser)
                .fetchJoin()
                .where(fromUserIdEq(fromUser, fromUserIdCond),
                        toUserIdEq(toUser, toUserIdCond),
                        statusEq(statusCond))
                .fetch();

        JPAQuery<Long> countQuery = getCount(fromUser, toUser, fromUserIdCond, toUserIdCond,
                statusCond);

        return PageableExecutionUtils.getPage(query, pageable, countQuery::fetchOne);
    }

    private BooleanExpression fromUserIdEq(QUser fromUser, UUID fromUserIdCond) {
        return fromUserIdCond != null ? fromUser.id.eq(fromUserIdCond) : null;
    }

    private BooleanExpression toUserIdEq(QUser toUser, UUID toUserIdCond) {
        return toUserIdCond != null ? toUser.id.eq(toUserIdCond) : null;
    }

    private BooleanExpression statusEq(FriendRequestStatus statusCond) {
        return statusCond != null ? friendRequest.status.eq(statusCond) : null;
    }

    private JPAQuery<Long> getCount(QUser fromUser, QUser toUser, UUID fromUserIdCond,
            UUID toUserIdCond, FriendRequestStatus statusCond) {
        return jpaQueryFactory
                .select(friendRequest.count())
                .where(fromUserIdEq(fromUser, fromUserIdCond),
                        toUserIdEq(toUser, toUserIdCond),
                        statusEq(statusCond))
                .from(friendRequest);
    }
}

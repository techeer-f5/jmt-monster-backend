package com.techeer.f5.jmtmonster.domain.friend.dao;

import static com.techeer.f5.jmtmonster.domain.friend.domain.QFriend.friend;
import static com.techeer.f5.jmtmonster.domain.friend.domain.QFriendRequest.friendRequest;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
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
public class FriendQueryRepositoryImpl implements FriendQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Friend> searchFriends(Pageable pageable, UUID fromUserIdCond, UUID toUserIdCond) {
        QUser fromUser = new QUser("fromUser");
        QUser toUser = new QUser("toUser");

        List<Friend> query = jpaQueryFactory
                .selectFrom(friend)
                .where(fromUserIdEq(fromUser, fromUserIdCond),
                        toUserIdEq(toUser, toUserIdCond))
                .innerJoin(friend.fromUser, fromUser)
                .fetchJoin()
                .innerJoin(friend.toUser, toUser)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = getCount(fromUser, toUser, fromUserIdCond, toUserIdCond);

        return PageableExecutionUtils.getPage(query, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCount(QUser fromUser, QUser toUser, UUID fromUserIdCond,
            UUID toUserIdCond) {
        return jpaQueryFactory
                .select(friendRequest.count())
                .from(friendRequest)
                .where(fromUserIdEq(fromUser, fromUserIdCond),
                        toUserIdEq(toUser, toUserIdCond));
    }

    private BooleanExpression fromUserIdEq(QUser fromUser, UUID fromUserIdCond) {
        return fromUserIdCond != null ? fromUser.id.eq(fromUserIdCond) : null;
    }

    private BooleanExpression toUserIdEq(QUser toUser, UUID toUserIdCond) {
        return toUserIdCond != null ? toUser.id.eq(toUserIdCond) : null;
    }
}

package com.techeer.f5.jmtmonster.domain.friend.dao;

import static com.techeer.f5.jmtmonster.domain.friend.domain.QFriend.friend;
import static com.techeer.f5.jmtmonster.domain.friend.domain.QFriendRequest.friendRequest;
import static com.techeer.f5.jmtmonster.domain.user.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
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
        List<Friend> query = jpaQueryFactory
                .selectFrom(friend)
                .where(fromUserIdEq(fromUserIdCond),
                        toUserIdEq(toUserIdCond))
                .innerJoin(friend.fromUser, user)
                .fetchJoin()
                .innerJoin(friend.toUser, user)
                .fetchJoin()
                .fetch();

        JPAQuery<Long> countQuery = getCount(fromUserIdCond, toUserIdCond);

        return PageableExecutionUtils.getPage(query, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCount(UUID fromUserIdCond, UUID toUserIdCond) {
        return jpaQueryFactory
                .select(friendRequest.count())
                .from(friendRequest)
                .where(fromUserIdEq(fromUserIdCond),
                        toUserIdEq(toUserIdCond));
    }

    private BooleanExpression fromUserIdEq(UUID fromUserIdCond) {
        return fromUserIdCond != null ? friend.fromUser.id.eq(fromUserIdCond) : null;
    }

    private BooleanExpression toUserIdEq(UUID toUserIdCond) {
        return toUserIdCond != null ? friend.toUser.id.eq(toUserIdCond) : null;
    }
}

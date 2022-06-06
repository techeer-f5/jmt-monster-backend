package com.techeer.f5.jmtmonster.domain.friend.dao;

import static com.techeer.f5.jmtmonster.domain.friend.domain.QFriend.friend;
import static com.techeer.f5.jmtmonster.domain.friend.domain.QFriendRequest.friendRequest;
import static com.techeer.f5.jmtmonster.domain.user.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
import com.techeer.f5.jmtmonster.domain.friend.domain.QFriend;
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
    public Page<Friend> searchFriends(Pageable pageable, UUID fromUserIdCond, UUID toUserIdCond,
            Boolean isHangingOutCond) {
        List<Friend> query = jpaQueryFactory
                .selectFrom(friend)
                .where(fromUserIdEq(fromUserIdCond),
                        toUserIdEq(toUserIdCond),
                        isHangingOutEq(isHangingOutCond))
                .innerJoin(friend.fromUser, user)
                .fetchJoin()
                .innerJoin(friend.toUser, user)
                .fetchJoin()
                .fetch();

        JPAQuery<Long> countQuery = getCount(fromUserIdCond, toUserIdCond, isHangingOutCond);

        return PageableExecutionUtils.getPage(query, pageable, countQuery::fetchOne);
    }

    @Override
    public long hangOutOffForAllFriendsOfFromUser(UUID fromUserIdCond) {
        QFriend subF = new QFriend("subF");
        QUser subU = new QUser("subU");

        // fromUserId == fromUserIdCond이면서 놀러가기 상태인 친구 ID
        BooleanExpression fromUserCond = friend.id.in(JPAExpressions
                .select(subF.id)
                .from(subF)
                .innerJoin(subF.fromUser, subU)
                .where(subU.id.eq(fromUserIdCond),
                        subF.isHangingOut.isTrue()));

        return jpaQueryFactory
                .update(friend)
                .set(friend.isHangingOut, false)
                .where(fromUserCond)
                .execute();
    }

    private JPAQuery<Long> getCount(UUID fromUserIdCond, UUID toUserIdCond,
            Boolean isHangingOutCond) {
        return jpaQueryFactory
                .select(friendRequest.count())
                .from(friendRequest)
                .where(fromUserIdEq(fromUserIdCond),
                        toUserIdEq(toUserIdCond),
                        isHangingOutEq(isHangingOutCond));
    }

    private BooleanExpression fromUserIdEq(UUID fromUserIdCond) {
        return fromUserIdCond != null ? friend.fromUser.id.eq(fromUserIdCond) : null;
    }

    private BooleanExpression toUserIdEq(UUID toUserIdCond) {
        return toUserIdCond != null ? friend.toUser.id.eq(toUserIdCond) : null;
    }

    private BooleanExpression isHangingOutEq(Boolean isHangingOutCond) {
        return isHangingOutCond != null ? friend.isHangingOut.eq(isHangingOutCond) : null;
    }
}

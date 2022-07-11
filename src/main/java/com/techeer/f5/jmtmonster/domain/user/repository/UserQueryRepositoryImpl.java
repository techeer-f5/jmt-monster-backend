package com.techeer.f5.jmtmonster.domain.user.repository;

import static com.techeer.f5.jmtmonster.domain.user.domain.QUser.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<User> searchByEmail(Pageable pageable, String emailCond) {
        List<User> query = jpaQueryFactory
                .selectFrom(user)
                .where(user.email.eq(emailCond))
                .fetch();

        JPAQuery<Long> countQuery = getCount(emailCond);

        return PageableExecutionUtils.getPage(query, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCount(String emailCond) {
        return jpaQueryFactory
                .select(user.count())
                .from(user)
                .where(user.email.eq(emailCond));
    }
}

package com.techeer.f5.jmtmonster.domain.home.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import com.techeer.f5.jmtmonster.domain.home.domain.HomeToUser;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.techeer.f5.jmtmonster.domain.home.domain.QHome;
import com.techeer.f5.jmtmonster.domain.home.domain.QHomeToUser;
import com.techeer.f5.jmtmonster.domain.user.domain.QUser;

@Repository
@RequiredArgsConstructor
public class HomeQueryRepositoryImpl implements HomeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final HomeToUserRepository homeToUserRepository;

    @Override
    public List<HomeToUser> findAllHomeToUsersByUser(User user) {
        return jpaQueryFactory.from(QUser.user)
                .where(QUser.user.id.eq(user.getId()))
                .innerJoin(QHomeToUser.homeToUser)
                .fetchJoin()
                .orderBy(QHomeToUser.homeToUser.updatedOn.desc())
                .select(QHomeToUser.homeToUser)
                .fetch();
    }

    @Override
    public List<Home> findAllHomesByUser(User user) {
        return jpaQueryFactory.from(QUser.user)
                .where(QUser.user.id.eq(user.getId()))
                .innerJoin(QHomeToUser.homeToUser)
                .fetchJoin()
                .innerJoin(QHome.home)
                .fetchJoin()
                .orderBy(QHome.home.updatedOn.desc())
                .select(QHome.home)
                .fetch();
    }

    @Override
    public Home findCurrentHomeByUser(User user) {
        return jpaQueryFactory.from(QUser.user)
                .where(QUser.user.id.eq(user.getId()))
                .innerJoin(QHomeToUser.homeToUser)
                .fetchJoin()
                .innerJoin(QHome.home)
                .fetchJoin()
                .select(QHome.home)
                .where(QHomeToUser.homeToUser.current.isTrue())
                .fetchOne();
    }

    @Override
    public void migrate(User user, Home home) {
        List<HomeToUser> previousHomes = jpaQueryFactory.selectFrom(QHomeToUser.homeToUser)
                .where(QHomeToUser.homeToUser.user.id.eq(user.getId()))
                .where(QHomeToUser.homeToUser.current.isTrue())
                .fetch();

        for (HomeToUser previousHome : previousHomes) {
            previousHome.setCurrent(false);
            homeToUserRepository.saveAndFlush(previousHome);
        }

        HomeToUser homeToUser = jpaQueryFactory.selectFrom(QHomeToUser.homeToUser)
                .where(QHomeToUser.homeToUser.user.id.eq(user.getId()))
                .where(QHomeToUser.homeToUser.home.id.eq(home.getId()))
                .fetchOne();

        if (homeToUser == null) {
            homeToUser = HomeToUser.builder()
                    .user(user)
                    .home(home)
                    .build();
        }

        homeToUser.setCurrent(true);

        homeToUserRepository.saveAndFlush(homeToUser);
    }

}

package com.techeer.f5.jmtmonster.domain.home.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import com.techeer.f5.jmtmonster.domain.home.domain.HomeToUser;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.techeer.f5.jmtmonster.domain.home.domain.QHome;
import com.techeer.f5.jmtmonster.domain.home.domain.QHomeToUser;
import com.techeer.f5.jmtmonster.domain.user.domain.QUser;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class HomeQueryRepositoryImpl implements HomeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final HomeToUserRepository homeToUserRepository;
    @Override
    public List<HomeToUser> findAllHomeToUsersByUser(User user) {
        return jpaQueryFactory.selectFrom(QHomeToUser.homeToUser)
                .innerJoin(QHomeToUser.homeToUser.user, QUser.user)
                .fetchJoin()
                .innerJoin(QHomeToUser.homeToUser.home, QHome.home)
                .fetchJoin()
                .where(QUser.user.id.eq(user.getId()))
                .orderBy(QHomeToUser.homeToUser.updatedOn.desc())
                .distinct()
                .fetch();
    }

    @Override
    public List<Home> findAllHomesByUser(User user) {
        return jpaQueryFactory.selectFrom(QHome.home)
                .innerJoin(QHome.home.homeToUsers, QHomeToUser.homeToUser)
                .fetchJoin()
                .innerJoin(QHomeToUser.homeToUser.user, QUser.user)
                .fetchJoin()
                .where(QUser.user.id.eq(user.getId()))
                .orderBy(QHome.home.updatedOn.desc())
                .distinct()
                .fetch();
    }

    @Override
    public Home findCurrentHomeByUser(User user) {
        return jpaQueryFactory.selectFrom(QHome.home)
                .innerJoin(QHome.home.homeToUsers, QHomeToUser.homeToUser)
                .fetchJoin()
                .innerJoin(QHomeToUser.homeToUser.user, QUser.user)
                .fetchJoin()
                .where(QUser.user.id.eq(user.getId()))
                .where(QHomeToUser.homeToUser.current.isTrue())
                .fetchOne();
    }

    @Override
    @Transactional
    public void migrate(User user, Home home) {
        List<HomeToUser> previousHomes = jpaQueryFactory.selectFrom(QHomeToUser.homeToUser)
                .innerJoin(QHomeToUser.homeToUser.user, QUser.user)
                .fetchJoin()
                .where(QHomeToUser.homeToUser.user.id.eq(user.getId()))
                .where(QHomeToUser.homeToUser.current.isTrue())
                .fetch();

        for (HomeToUser previousHome : previousHomes) {
            previousHome.setCurrent(false);
            homeToUserRepository.saveAndFlush(previousHome);
        }

        HomeToUser homeToUser = jpaQueryFactory.selectFrom(QHomeToUser.homeToUser)
                .innerJoin(QHomeToUser.homeToUser.user, QUser.user)
                .fetchJoin()
                .innerJoin(QHomeToUser.homeToUser.home, QHome.home)
                .fetchJoin()
                .where(QHomeToUser.homeToUser.user.id.eq(user.getId()))
                .where(QHomeToUser.homeToUser.home.id.eq(home.getId()))
                .orderBy(QHomeToUser.homeToUser.updatedOn.desc())
                .fetchOne();

        if (homeToUser == null) {
            homeToUser = HomeToUser.builder()
                    .user(user)
                    .home(home)
                    .build();
        }

        user.setAddress(home.getName());

        homeToUser.setCurrent(true);

        homeToUserRepository.saveAndFlush(homeToUser);
    }

}

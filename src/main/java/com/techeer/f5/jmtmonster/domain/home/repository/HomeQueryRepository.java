package com.techeer.f5.jmtmonster.domain.home.repository;

import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import com.techeer.f5.jmtmonster.domain.home.domain.HomeToUser;
import com.techeer.f5.jmtmonster.domain.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface HomeQueryRepository {

    List<HomeToUser> findAllHomeToUsersByUser(User user);
    List<Home> findAllHomesByUser(User user);
    Home findCurrentHomeByUser(User user);
    void migrate(User user, Home home);
}

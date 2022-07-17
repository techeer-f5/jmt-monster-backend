package com.techeer.f5.jmtmonster.domain.user.repository;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryRepository {

    Page<User> searchByEmail(Pageable pageable, String emailCond);
}

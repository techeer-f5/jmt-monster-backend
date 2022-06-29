package com.techeer.f5.jmtmonster.domain.home.repository;

import com.techeer.f5.jmtmonster.domain.home.domain.HomeToUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HomeToUserRepository extends JpaRepository<HomeToUser, UUID> {

}

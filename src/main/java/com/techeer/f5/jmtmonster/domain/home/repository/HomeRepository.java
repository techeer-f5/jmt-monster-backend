package com.techeer.f5.jmtmonster.domain.home.repository;

import com.techeer.f5.jmtmonster.domain.home.domain.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HomeRepository extends JpaRepository<Home, UUID>, HomeQueryRepository {

    Home findHomeByCode(String code);

}

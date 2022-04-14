package com.techeer.f5.jmtmonster.domain.oauth.repository;

import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, UUID> {

}

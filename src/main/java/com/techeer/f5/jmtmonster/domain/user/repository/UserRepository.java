package com.techeer.f5.jmtmonster.domain.user.repository;

import com.techeer.f5.jmtmonster.domain.user.domain.AuthProvider;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> getUserByName(String name);

    default User build(String name, String email, AuthProvider provider) {
        return User.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .build();
    }
}

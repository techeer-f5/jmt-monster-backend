package com.techeer.f5.jmtmonster.domain.user.repository;

import com.techeer.f5.jmtmonster.domain.user.domain.AuthProvider;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    default User createAndSave(String name, String email, AuthProvider provider) {
        User user = User.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .build();

        save(user);
        return user;
    }
}

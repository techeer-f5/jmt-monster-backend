package com.techeer.f5.jmtmonster.domain.user.repository;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, UserQueryRepository {

    Optional<User> getUserByName(String name);

    Optional<User> getUserByEmail(String email);

    @Transactional
    default User build(String name, String email) {
        User user = User.builder()
                .name(name)
                .email(email)
                .build();

        return save(user);
    }
}

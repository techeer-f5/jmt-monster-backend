package com.techeer.f5.jmtmonster.domain.oauth.repository;

import com.techeer.f5.jmtmonster.domain.oauth.domain.AuthProvider;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.UUID;

@Repository
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, UUID> {

    @Transactional
    default PersistentToken build(User user, AuthProvider authProvider) {
        PersistentToken persistentToken = PersistentToken.builder()
                .user(user)
                .provider(authProvider)
                .build();

        persistentToken = saveAndFlush(persistentToken);

        user.addToken(persistentToken);

        return persistentToken;
    }
}


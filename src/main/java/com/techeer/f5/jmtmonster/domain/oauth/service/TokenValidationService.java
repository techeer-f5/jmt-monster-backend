package com.techeer.f5.jmtmonster.domain.oauth.service;

import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenValidationService {
    private final PersistentTokenRepository persistentTokenRepository;

    public User validate(UUID token) {
        Optional<PersistentToken> optionalPersistentToken = persistentTokenRepository.findById(token);

        if (optionalPersistentToken.isEmpty()) {
            return null;
        }

        return optionalPersistentToken.get().getUser();
    }
}

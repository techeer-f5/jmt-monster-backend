package com.techeer.f5.jmtmonster.domain.user.service;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.UserDto;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<UserDto> findOne(UUID userId) {
        return userRepository.findById(userId).map(User::convert);
    }
}

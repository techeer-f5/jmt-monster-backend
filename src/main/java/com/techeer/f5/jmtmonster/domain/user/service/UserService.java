package com.techeer.f5.jmtmonster.domain.user.service;

import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.user.dto.UserResponseDto;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.ExtraUserInfoRequestDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.error.exception.CustomStatusException;
import com.techeer.f5.jmtmonster.global.error.exception.ErrorCode;
import com.techeer.f5.jmtmonster.global.error.exception.NotAuthorizedException;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PersistentTokenRepository persistentTokenRepository;
    private final UserMapper userMapper;

    public UUID getTokenId(HttpServletRequest request) {

        String userId = (String) request.getAttribute("tokenId");

        if (userId == null) {
            throw new NotAuthorizedException("토큰이 주어지지 않았습니다.");
        }

        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException exception) {
            log.error(exception.toString());
            throw new NotAuthorizedException("토큰 UUID 파싱에 실패했습니다.");
        }
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByTokenId(UUID tokenId) {
        Optional<PersistentToken> optionalPersistentToken = persistentTokenRepository.findById(tokenId);

        if (optionalPersistentToken.isEmpty()) {
            return Optional.empty();
        }

        PersistentToken persistentToken = optionalPersistentToken.get();

        return Optional.ofNullable(persistentToken.getUser());
    }

    public User findUserWithRequest(HttpServletRequest request) {
        UUID tokenId = getTokenId(request);
        Optional<User> optionalUser = findUserByTokenId(tokenId);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("user", "tokenId", "");
        }

        return optionalUser.get();
    }

    public UserResponseDto getMyUser(HttpServletRequest request) {
        User user = findUserWithRequest(request);

        UserDto userDto = userMapper.toUserDto(user);

        return UserResponseDto.builder()
                .success(true)
                .user(userDto)
                .build();
    }

    @Transactional
    public UserResponseDto submitExtraInfo(HttpServletRequest request,
            ExtraUserInfoRequestDto extraUserInfoRequestDto) {
        User user = findUserWithRequest(request);

        if (user.getExtraInfoInjected()) {
            throw new CustomStatusException(ErrorCode.BAD_REQUEST, "사용자 추가 정보가 이미 입력되어 있습니다.");
        }

        try {
            user.addExtraInfo(
                    extraUserInfoRequestDto.getNickname(),
                    extraUserInfoRequestDto.getAddress(),
                    extraUserInfoRequestDto.getImageUrl());
        } catch (IllegalStateException exception) {
            throw new CustomStatusException(ErrorCode.CONFLICT, exception.getMessage());
        }

        return userMapper.toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateExtraInfo(HttpServletRequest request,
            ExtraUserInfoRequestDto extraUserInfoRequestDto) {
        User user = findUserWithRequest(request);

        if (!user.getExtraInfoInjected()) {
            throw new CustomStatusException(ErrorCode.BAD_REQUEST, "사용자 추가 정보가 기존에 입력되지 않았습니다.");
        }

        try {
            user.addExtraInfo(
                    extraUserInfoRequestDto.getNickname(),
                    extraUserInfoRequestDto.getAddress(),
                    extraUserInfoRequestDto.getImageUrl());
        } catch (IllegalStateException exception) {
            throw new CustomStatusException(ErrorCode.CONFLICT, exception.getMessage());
        }

        return userMapper.toUserResponseDto(user);
    }
}

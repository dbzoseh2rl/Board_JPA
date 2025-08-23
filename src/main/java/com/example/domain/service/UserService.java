package com.example.domain.service;

import com.example.domain.dto.user.UserResponse;
import com.example.domain.dto.user.UserWithTokenResponse;
import com.example.domain.entity.User;
import com.example.domain.repository.UserRepository;
import com.example.global.exception.UserNotFoundException;
import com.example.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public User get(long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User signin(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateKeyException("username already exists");
        }

        return userRepository.save(user);
    }

    public UserWithTokenResponse login(UserResponse userResponse) {
        User user = getUserEmail(userResponse);

        // 비밀번호 검증
        if (!userResponse.password().equals(user.getPassword())) {
            throw new UserNotFoundException();
        }

        return generateToken(user);
    }

    public UserWithTokenResponse refreshToken(long userId) {
        return generateToken(get(userId));
    }

    private User getUserEmail(UserResponse userResponse) {
        return userRepository.findByEmail(userResponse.id()).orElseThrow(UserNotFoundException::new);
    }

    private UserWithTokenResponse generateToken(User user) {
        return new UserWithTokenResponse(
                user.getEmail(),
                jwtUtil.generate(user.getId(), "ACCESS"),
                jwtUtil.generate(user.getId(), "REFRESH")
        );
    }

}

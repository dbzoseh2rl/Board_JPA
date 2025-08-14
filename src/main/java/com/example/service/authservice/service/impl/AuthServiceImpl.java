package com.example.service.authservice.service.impl;

import com.example.dto.User;
import com.example.dto.UserWithToken;
import com.example.model.Member;
import com.example.repository.AuthRepository;
import com.example.service.authservice.service.AuthService;
import com.example.global.common.exception.UserNotFoundException;
import com.example.global.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public Member signin(Member member) {
        if (authRepository.existsByUserId(member.getUserId())) {
            throw new DuplicateKeyException("username already exists");
        }

        return authRepository.save(member);
    }

    @Override
    public UserWithToken login(User user) {
        Member member = authRepository.findByUserId(user.getId())
                .orElseThrow(UserNotFoundException::new);

        // 비밀번호 검증
        if (!user.getPassword().equals(member.getPassword())) {
            throw new UserNotFoundException();
        }

        return generateToken(member);
    }

    @Override
    public UserWithToken refreshToken(long userSeq) {
        Member member = authRepository.findById(userSeq)
                .orElseThrow(UserNotFoundException::new);

        return generateToken(member);
    }

    private UserWithToken generateToken(Member member) {
        return new UserWithToken(
                member.getUserId(),
                jwtUtil.generate(member.getMemberNo(), "ACCESS"),
                jwtUtil.generate(member.getMemberNo(), "REFRESH")
        );
    }
}

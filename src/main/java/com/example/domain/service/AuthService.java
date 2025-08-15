package com.example.domain.service;

import com.example.domain.dto.user.UserResponse;
import com.example.domain.dto.user.UserWithTokenResponse;
import com.example.domain.entity.Member;
import com.example.domain.repository.AuthRepository;
import com.example.global.common.exception.UserNotFoundException;
import com.example.global.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Member signin(Member member) {
        if (authRepository.existsByUserId(member.getUserId())) {
            throw new DuplicateKeyException("username already exists");
        }

        return authRepository.save(member);
    }

    public UserWithTokenResponse login(UserResponse userResponse) {
        Member member = authRepository.findByUserId(userResponse.id())
                .orElseThrow(UserNotFoundException::new);

        // 비밀번호 검증
        if (!userResponse.password().equals(member.getPassword())) {
            throw new UserNotFoundException();
        }

        return generateToken(member);
    }

    public UserWithTokenResponse refreshToken(long userSeq) {
        Member member = authRepository.findById(userSeq)
                .orElseThrow(UserNotFoundException::new);

        return generateToken(member);
    }

    private UserWithTokenResponse generateToken(Member member) {
        return new UserWithTokenResponse(
                member.getUserId(),
                jwtUtil.generate(member.getId(), "ACCESS"),
                jwtUtil.generate(member.getId(), "REFRESH")
        );
    }

    public Member get(long id){
        return authRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

}

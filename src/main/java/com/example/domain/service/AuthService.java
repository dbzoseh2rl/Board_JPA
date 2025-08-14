package com.example.domain.service;

import com.example.domain.dto.User;
import com.example.domain.dto.UserWithToken;
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

    public UserWithToken login(User user) {
        Member member = authRepository.findByUserId(user.getId())
                .orElseThrow(UserNotFoundException::new);

        // 비밀번호 검증
        if (!user.getPassword().equals(member.getPassword())) {
            throw new UserNotFoundException();
        }

        return generateToken(member);
    }

    public UserWithToken refreshToken(long userSeq) {
        Member member = authRepository.findById(userSeq)
                .orElseThrow(UserNotFoundException::new);

        return generateToken(member);
    }

    private UserWithToken generateToken(Member member) {
        return new UserWithToken(
                member.getUserId(),
                jwtUtil.generate(member.getId(), "ACCESS"),
                jwtUtil.generate(member.getId(), "REFRESH")
        );
    }

    public Member get(long id){
        return authRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

}

package com.example.service.authservice.service;

import com.example.dto.User;
import com.example.dto.UserWithToken;
import com.example.model.Member;

import java.util.Optional;

public interface AuthService {
    Member signin(Member member);
    UserWithToken login(User user);
    UserWithToken refreshToken(long userSeq);

    Optional<Member> get(long userSeq);
}

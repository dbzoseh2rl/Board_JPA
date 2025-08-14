package com.example.auth.service;

import com.example.auth.dto.User;
import com.example.auth.dto.UserWithToken;
import com.example.auth.model.Member;

import java.util.Optional;

public interface AuthService {
    Member signin(Member member);
    UserWithToken login(User user);
    UserWithToken refreshToken(long userSeq);

    Optional<Member> get(long userSeq);
}

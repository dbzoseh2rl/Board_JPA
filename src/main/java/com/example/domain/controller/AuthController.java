package com.example.domain.controller;

import com.example.domain.dto.user.UserResponse;
import com.example.domain.dto.user.UserWithTokenResponse;
import com.example.domain.entity.Member;
import com.example.domain.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auths")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value="/signin")
    public Member signin(@Valid @RequestBody UserResponse userResponse) {
        return authService.signin(Member.from(userResponse));
    }

    @PostMapping(value="/login")
    public UserWithTokenResponse login(@Valid @RequestBody UserResponse userResponse) {
        return authService.login(userResponse);
    }

    @PostMapping(value="/logout")
    public Object logout() {
        return null;
    }

    @GetMapping(value="/refresh")
    public UserWithTokenResponse refreshToken(@RequestAttribute long userSeq) {
        return authService.refreshToken(userSeq);
    }
}

package com.example.domain.controller;

import com.example.domain.dto.User;
import com.example.domain.dto.UserWithToken;
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
    public Member signin(@Valid @RequestBody User user) {
        return authService.signin(Member.from(user));
    }

    @PostMapping(value="/login")
    public UserWithToken login(@Valid @RequestBody User user) {
        return authService.login(user);
    }

    @PostMapping(value="/logout")
    public Object logout() {
        return null;
    }

    @GetMapping(value="/refresh")
    public UserWithToken refreshToken(@RequestAttribute long userSeq) {
        return authService.refreshToken(userSeq);
    }
}

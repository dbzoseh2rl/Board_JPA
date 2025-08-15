package com.example.domain.controller;

import com.example.domain.dto.user.UserResponse;
import com.example.domain.dto.user.UserWithTokenResponse;
import com.example.domain.entity.User;
import com.example.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signin")
    public User signin(@Valid @RequestBody UserResponse userResponse) {
        return userService.signin(User.from(userResponse));
    }

    @PostMapping("/login")
    public UserWithTokenResponse login(@Valid @RequestBody UserResponse userResponse) {
        return userService.login(userResponse);
    }

    @PostMapping("/logout")
    public void logout() {
    }

    @GetMapping("/refresh")
    public UserWithTokenResponse refreshToken(@RequestAttribute long userSeq) {
        return userService.refreshToken(userSeq);
    }

}

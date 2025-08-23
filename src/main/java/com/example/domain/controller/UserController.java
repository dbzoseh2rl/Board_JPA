package com.example.domain.controller;

import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.user.UserResponse;
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
    public ApiResponse signin(@Valid @RequestBody UserResponse userResponse) {
        return ApiResponse.of(userService.signin(User.from(userResponse)));
    }

    @PostMapping("/login")
    public ApiResponse login(@Valid @RequestBody UserResponse userResponse) {
        return ApiResponse.of(userService.login(userResponse));
    }

    @PostMapping("/logout")
    public void logout() {
    }

    @GetMapping("/refresh")
    public ApiResponse refreshToken(@RequestAttribute long userId) {
        return ApiResponse.of(userService.refreshToken(userId));
    }

}

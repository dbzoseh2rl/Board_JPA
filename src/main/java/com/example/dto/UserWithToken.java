package com.example.dto;

import com.example.dto.User;

public class UserWithToken extends User {
    public UserWithToken(String id, String accessToken, String refreshToken) {
        super(id, accessToken, refreshToken);
    }
}

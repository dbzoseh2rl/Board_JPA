package com.example.domain.dto;

public class UserWithToken extends User {
    public UserWithToken(String id, String accessToken, String refreshToken) {
        super(id, accessToken, refreshToken);
    }
}

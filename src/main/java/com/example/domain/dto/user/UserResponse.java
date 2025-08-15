package com.example.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
        @NotEmpty String id,
        String password,
        String accessToken,
        String refreshToken
) {
    // 정적 팩토리 메서드들
    public static UserResponse basic(String id, String password) {
        return new UserResponse(id, password, null, null);
    }

    public static UserResponse withTokens(String id, String accessToken, String refreshToken) {
        return new UserResponse(id, null, accessToken, refreshToken);
    }

    public boolean hasTokens() {
        return accessToken != null && refreshToken != null;
    }
}
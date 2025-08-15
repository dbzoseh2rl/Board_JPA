package com.example.domain.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record UserWithTokenResponse(
        @NotEmpty String id,
        String accessToken,
        String refreshToken
) {
}
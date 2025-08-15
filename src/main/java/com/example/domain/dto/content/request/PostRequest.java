package com.example.domain.dto.content.request;

import jakarta.validation.constraints.NotEmpty;

public record PostRequest(
        @NotEmpty
        String title,
        @NotEmpty
        String content
) {
}
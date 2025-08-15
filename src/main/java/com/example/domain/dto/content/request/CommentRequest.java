package com.example.domain.dto.content.request;

import jakarta.validation.constraints.NotEmpty;

public record CommentRequest(
        @NotEmpty
        String content
) {
}

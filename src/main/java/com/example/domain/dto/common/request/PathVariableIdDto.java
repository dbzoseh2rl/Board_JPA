package com.example.domain.dto.common.request;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;

public record PathVariableIdDto(

        @RequestAttribute Long userId,
        @PathVariable("boardId") Long boardId,
        @PathVariable("postId") Long postId,
        @PathVariable(value = "commentId", required = false) Long commentId
) {
    // record는 모든 필드가 자동으로 final이며, getter는 필드명() 형태로 제공됩니다.
}

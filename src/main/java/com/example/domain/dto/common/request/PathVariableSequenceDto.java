package com.example.domain.dto.common.request;

public record PathVariableSequenceDto(
        long userSeq,
        long boardSeq,
        long postSeq,
        long commentSeq
) {
    // record는 모든 필드가 자동으로 final이며, getter는 필드명() 형태로 제공됩니다.
}
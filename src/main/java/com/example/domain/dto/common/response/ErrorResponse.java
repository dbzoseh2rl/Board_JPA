package com.example.domain.dto.common.response;

import com.example.domain.dto.common.ResultType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public record ErrorResponse(ResultType resultType) {

    @JsonIgnore
    public HttpStatus status() {
        return resultType != null ? resultType.getStatus() : null;
    }

    public String code() {
        return resultType != null ? resultType.getCode() : null;
    }

    public String message() {
        return resultType != null ? resultType.getMessage() : null;
    }

}

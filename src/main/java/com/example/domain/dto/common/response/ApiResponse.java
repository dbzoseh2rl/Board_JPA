package com.example.domain.dto.common.response;

import com.example.domain.dto.common.ResultType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(

        ResultType resultType,
        Object data

) {

    public static ApiResponse of(ResultType resultType) {
        return new ApiResponse(resultType, null);
    }

    public static ApiResponse of(Object data) {
        return new ApiResponse(ResultType.OK, data);
    }

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

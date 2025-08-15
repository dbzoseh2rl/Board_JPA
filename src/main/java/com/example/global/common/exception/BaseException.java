package com.example.global.common.exception;

import com.example.domain.dto.common.response.ErrorResponse;
import com.example.domain.dto.common.ResultType;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1733247474863885433L;

    private final ResultType resultType;

    public BaseException(ResultType resultType) {
        super(resultType.getMessage());
        this.resultType = resultType;
    }

    public BaseException(ResultType resultType, String message) {
        super(message);
        this.resultType = resultType;
    }

    public ErrorResponse getExceptionResult() {
        return new ErrorResponse(this.resultType);
    }
}

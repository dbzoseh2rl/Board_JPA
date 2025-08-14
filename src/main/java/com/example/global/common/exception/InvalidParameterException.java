package com.example.global.common.exception;

import com.example.domain.dto.ResultType;

public class InvalidParameterException extends BaseException {
    private static final long serialVersionUID = -4011164633307762223L;

    public InvalidParameterException() {
        super(ResultType.INVALID_PARAMETER);
    }
}

package com.example.global.exception;

import com.example.domain.dto.common.ResultType;

public class AccessTokenRequiredException extends BaseException {

    private static final long serialVersionUID = 2748885175496513946L;

    public AccessTokenRequiredException(String message) {
        super(ResultType.ACCESS_TOKEN_REQUIRED, message);
    }
}

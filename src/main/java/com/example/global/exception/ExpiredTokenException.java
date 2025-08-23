package com.example.global.exception;

import com.example.domain.dto.common.ResultType;

public class ExpiredTokenException extends BaseException {

    private static final long serialVersionUID = -6564973261837433080L;

    public ExpiredTokenException() {
        super(ResultType.EXPIRED_TOKEN);
    }
}

package com.example.global.common.exception;

import com.example.domain.dto.ResultType;

public class NotAllowedOperationException extends BaseException {

    private static final long serialVersionUID = -2759656177973979953L;

    public NotAllowedOperationException() {
        super(ResultType.NOT_ALLOWED_OPERATION);
    }
}

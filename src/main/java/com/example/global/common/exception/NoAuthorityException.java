package com.example.global.common.exception;

import com.example.dto.ResultType;

public class NoAuthorityException extends BaseException {

    private static final long serialVersionUID = 323183528761167051L;

    public NoAuthorityException() {
        super(ResultType.NO_ROLE);
    }
}

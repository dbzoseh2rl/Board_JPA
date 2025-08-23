package com.example.global.exception;

import com.example.domain.dto.common.ResultType;

public class DataNotFoundException extends BaseException {

    private static final long serialVersionUID = -7530170088983506216L;

    public DataNotFoundException() {
        super(ResultType.DATA_NOT_FOUND);
    }
}

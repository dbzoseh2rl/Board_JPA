package com.example.global.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Deprecated
public class Timestamp {

    // TODO: Audit 적용하기

    private Date regDt;
    private Date updDt;

}
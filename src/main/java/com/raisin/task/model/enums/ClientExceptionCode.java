package com.raisin.task.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public enum ClientExceptionCode {
    BAD_PARSING("G-1000", INTERNAL_SERVER_ERROR),
    BLOCKED("G-1001");
    private final String code;
    private final HttpStatus httpStatus;


    ClientExceptionCode(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    ClientExceptionCode(String code) {
        this(code, BAD_REQUEST);
    }

}

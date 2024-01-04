package com.raisin.task.exception;

import com.raisin.task.model.enums.ClientExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus statusCode;
    private final String id;
    private final ClientExceptionCode exceptionCode;

    public CustomException(ClientExceptionCode exceptionCode, String id) {
        super(exceptionCode.getCode());
        this.statusCode = exceptionCode.getHttpStatus();
        this.exceptionCode = exceptionCode;
        this.id = id;
    }
}

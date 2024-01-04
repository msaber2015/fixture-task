package com.raisin.task.exception;

import com.raisin.task.model.enums.ClientExceptionCode;

public class CustomRestException extends CustomException {

    public CustomRestException(ClientExceptionCode exceptionCode, String id) {
        super(exceptionCode,id);
    }
}

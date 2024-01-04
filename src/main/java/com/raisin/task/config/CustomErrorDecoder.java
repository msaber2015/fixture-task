package com.raisin.task.config;

import com.raisin.task.exception.CustomRestException;
import com.raisin.task.model.enums.ClientExceptionCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
        Response.Body responseBody = response.body();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        ClientExceptionCode exceptionCode = switch (responseStatus){
            case NOT_ACCEPTABLE -> ClientExceptionCode.BLOCKED;
            default -> ClientExceptionCode.BAD_PARSING;
        };
        return new CustomRestException(exceptionCode, responseBody.toString());
    }
}

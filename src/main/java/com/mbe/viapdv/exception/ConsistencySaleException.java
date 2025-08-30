package com.mbe.viapdv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConsistencySaleException extends RuntimeException {
    public ConsistencySaleException(String message) {
        super(message);
    }
}

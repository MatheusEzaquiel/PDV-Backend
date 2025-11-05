package com.mbe.viapdv.exception;

public class UserAlredyExistException extends RuntimeException {
    public UserAlredyExistException(String message) {
        super(message);
    }
}

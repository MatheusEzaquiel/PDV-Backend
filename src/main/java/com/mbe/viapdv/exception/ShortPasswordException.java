package com.mbe.viapdv.exception;

public class ShortPasswordException extends RuntimeException {
    public ShortPasswordException(String message) {
        super(message);
    }
}

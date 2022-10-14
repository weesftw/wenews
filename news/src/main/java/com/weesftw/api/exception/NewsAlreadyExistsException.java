package com.weesftw.api.exception;

public class NewsAlreadyExistsException extends RuntimeException {

    public NewsAlreadyExistsException(String message) {
        super(message);
    }
}

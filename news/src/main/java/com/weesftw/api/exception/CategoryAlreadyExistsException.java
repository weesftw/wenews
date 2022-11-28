package com.weesftw.api.exception;

public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}

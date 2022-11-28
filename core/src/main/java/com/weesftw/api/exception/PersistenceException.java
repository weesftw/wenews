package com.weesftw.api.exception;

public class PersistenceException extends RuntimeException {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}

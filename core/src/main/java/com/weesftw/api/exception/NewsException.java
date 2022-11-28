package com.weesftw.api.exception;

public class NewsException extends RuntimeException {

    public NewsException(String message) {
        super(message);
    }

    public NewsException(Throwable cause) {
        super(cause);
    }
}

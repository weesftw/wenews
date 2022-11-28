package com.weesftw.api.exception;

public class QualityNewsException extends RuntimeException {

    public QualityNewsException() {
        super("News is not valid");
    }

    public QualityNewsException(String message) {
        super(message);
    }
}

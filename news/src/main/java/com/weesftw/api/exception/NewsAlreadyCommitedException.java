package com.weesftw.api.exception;

public class NewsAlreadyCommitedException extends RuntimeException {

    public NewsAlreadyCommitedException(String message) {
        super(message);
    }
}

package com.weesftw.api.exception;

public class UserBannedException extends RuntimeException {

    public UserBannedException() {
    }

    public UserBannedException(String message) {
        super(message);
    }
}

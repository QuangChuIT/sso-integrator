package com.bsc.sso.authentication.token;

public class TokenException extends RuntimeException {
    public TokenException() {
    }

    public TokenException(String msg) {
        super(msg);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    public TokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

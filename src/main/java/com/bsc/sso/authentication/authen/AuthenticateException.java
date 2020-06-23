package com.bsc.sso.authentication.authen;

public class AuthenticateException extends RuntimeException {
    public AuthenticateException() {
        super();
    }

    public AuthenticateException(String msg) {
        super(msg);
    }

    public AuthenticateException(Throwable cause) {
        super(cause);
    }

    public AuthenticateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

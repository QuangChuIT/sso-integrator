package com.bsc.sso.authentication.loginurl;

public class LoginUrlException extends RuntimeException {
    public LoginUrlException() {
        super();
    }

    public LoginUrlException(String msg) {
        super(msg);
    }

    public LoginUrlException(Throwable cause) {
        super(cause);
    }

    public LoginUrlException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

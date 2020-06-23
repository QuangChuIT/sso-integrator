package com.bsc.sso.authentication.logout;

public class LogoutException extends RuntimeException {
    public LogoutException() {
        super();
    }

    public LogoutException(String msg) {
        super(msg);
    }

    public LogoutException(Throwable cause) {
        super(cause);
    }

    public LogoutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

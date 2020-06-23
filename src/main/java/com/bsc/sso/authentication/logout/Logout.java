package com.bsc.sso.authentication.logout;

import javax.servlet.http.HttpServletRequest;

public interface Logout {
    /**
     * verify and get info user
     *
     * @param request
     * @return
     * @throws LogoutException
     */
    boolean logout(HttpServletRequest request) throws LogoutException;
}

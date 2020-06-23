package com.bsc.sso.authentication.loginurl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface LoginUrl {
    /**
     * verify and get info user
     *
     * @param request
     * @return
     * @throws LoginUrlException
     */
    String getLoginUrl(HttpServletRequest request) throws LoginUrlException;
}

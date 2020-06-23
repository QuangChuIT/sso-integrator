package com.bsc.sso.authentication.authen;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface Authenticate {
    /**
     * verify and get info user
     *
     * @param request
     * @return
     * @throws AuthenticateException
     */
    Map<String, String> verify(HttpServletRequest request) throws AuthenticateException;
}

package com.bsc.sso.authentication.authen.cas;

import com.bsc.sso.authentication.authen.Authenticate;
import com.bsc.sso.authentication.authen.AuthenticateException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class CasAuthenticate implements Authenticate {
    
    /**
     * verify and get info user
     *
     * @param request
     * @return
     * @throws AuthenticateException
     */
    @Override
    public Map<String, String> verify(HttpServletRequest request) throws AuthenticateException {
        return null;
    }
}

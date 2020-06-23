package com.bsc.sso.authentication.authen;

import com.bsc.sso.authentication.authen.bkav.BkavAuthenticate;
import com.bsc.sso.authentication.authen.vps.VPSAuthenticate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AuthenticateFactory {
    public Map<String, String> authentication(HttpServletRequest request, String type) throws AuthenticateException {
        Authenticate authenticate = null;

        switch (type) {
            case "bkav":
                authenticate = new BkavAuthenticate();
                break;
            case "vps":
                authenticate = new VPSAuthenticate();
                break;
            default:
                throw new AuthenticateException("Method login is not support");
        }

        return authenticate.verify(request);
    }
}

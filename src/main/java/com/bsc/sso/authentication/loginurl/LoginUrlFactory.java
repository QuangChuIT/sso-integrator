package com.bsc.sso.authentication.loginurl;

import com.bsc.sso.authentication.loginurl.bkav.BkavLoginUrl;
import com.bsc.sso.authentication.loginurl.vps.VPSLoginUrl;

import javax.servlet.http.HttpServletRequest;

public class LoginUrlFactory {
    public String getLoginUrl(HttpServletRequest request, String type) throws LoginUrlException {
        LoginUrl loginUrl = null;

        switch (type) {
            case "bkav":
                loginUrl = new BkavLoginUrl();
                break;
            case "vps":
                loginUrl = new VPSLoginUrl();
                break;
            default:
                throw new LoginUrlException("Method login is not support");
        }

        return loginUrl.getLoginUrl(request);
    }
}

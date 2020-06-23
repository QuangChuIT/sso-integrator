package com.bsc.sso.authentication.logout;

import com.bsc.sso.authentication.logout.bkav.BkavLogout;
import com.bsc.sso.authentication.logout.vps.VPSLogout;

import javax.servlet.http.HttpServletRequest;

public class LogoutFactory {
    public boolean logout(HttpServletRequest request, String type) throws LogoutException {
        Logout logout = null;

        switch (type) {
            case "bkav":
                logout = new BkavLogout();
                break;
            case "vps":
                logout = new VPSLogout();
                break;
            default:
                throw new LogoutException("Method logout is not support");
        }

        return logout.logout(request);
    }
}

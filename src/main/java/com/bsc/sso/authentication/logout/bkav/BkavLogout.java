package com.bsc.sso.authentication.logout.bkav;

import com.bsc.sso.authentication.logout.Logout;
import com.bsc.sso.authentication.logout.LogoutException;

import javax.servlet.http.HttpServletRequest;

public class BkavLogout implements Logout {

    @Override
    public boolean logout(HttpServletRequest request) throws LogoutException {
        return true;
    }
}

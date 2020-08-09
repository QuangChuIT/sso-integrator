package com.bsc.sso.authentication.loginurl.cas;

import com.bsc.sso.authentication.loginurl.LoginUrl;
import com.bsc.sso.authentication.loginurl.LoginUrlException;
import com.bsc.sso.authentication.util.ConfigUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

public class CasLoginUrl implements LoginUrl {
    @Override
    public String getLoginUrl(HttpServletRequest request) throws LoginUrlException {
        String casLoginEndpoint = ConfigUtil.getInstance().getProperty("cas.login.endpoint");
        String ssoCallbackUrl = ConfigUtil.getInstance().getProperty("callbackUri");
        return MessageFormat.format(casLoginEndpoint, ssoCallbackUrl);
    }
}

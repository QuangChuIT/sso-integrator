package com.bsc.sso.authentication.loginurl.bkav;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.loginurl.LoginUrl;
import com.bsc.sso.authentication.loginurl.LoginUrlException;
import com.bsc.sso.authentication.soap.SsoCheckResponse;
import com.bsc.sso.authentication.soap.WebService;
import com.bsc.sso.authentication.soap.WebServiceLocator;
import com.bsc.sso.authentication.soap.WebServiceSoap_PortType;
import com.bsc.sso.authentication.util.CommonUtil;
import com.bsc.sso.authentication.util.ConfigUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BkavLoginUrl implements LoginUrl {

    @Override
    public String getLoginUrl(HttpServletRequest request) throws LoginUrlException {
        return ConfigUtil.getInstance().getProperty(SSOAuthenticationConstants.CONNECT_SSO_SERVER_URL) + "?ReturnURL=" + CommonUtil.getFullURLSSORedirect(request);
    }
}

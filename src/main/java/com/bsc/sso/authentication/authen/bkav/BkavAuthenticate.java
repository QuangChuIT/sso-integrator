package com.bsc.sso.authentication.authen.bkav;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.authen.Authenticate;
import com.bsc.sso.authentication.authen.AuthenticateException;
import com.bsc.sso.authentication.soap.SsoCheckResponse;
import com.bsc.sso.authentication.soap.WebService;
import com.bsc.sso.authentication.soap.WebServiceLocator;
import com.bsc.sso.authentication.soap.WebServiceSoap_PortType;
import com.bsc.sso.authentication.util.ConfigUtil;
import com.bsc.sso.authentication.util.MemcacheUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BkavAuthenticate implements Authenticate {

    @Override
    public Map<String, String> verify(HttpServletRequest request) throws AuthenticateException {
        Map<String, String> result = new HashMap<>();
        // get cookie SSO
        Cookie[] cookies = request.getCookies();
        String ssoCookie = null;
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            if (name.equals(ConfigUtil.getInstance().getProperty(SSOAuthenticationConstants.CONNECT_SSO_COOKIE_NAME))) {
                ssoCookie = cookies[i].getValue();
                break;
            }
        }
        if (ssoCookie != null) {
            try {
                WebService webService = new WebServiceLocator();
                WebServiceSoap_PortType portType = webService.getWebServiceSoap();
                SsoCheckResponse ssoCheckResponse = portType.checkAuthToken(ssoCookie);
                String account = ssoCheckResponse.getUserName();
                // add user info to cache
                result.put("username", account);
                result.put("company", "bkav");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

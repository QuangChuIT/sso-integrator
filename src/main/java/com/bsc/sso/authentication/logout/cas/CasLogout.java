package com.bsc.sso.authentication.logout.cas;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.http.SendRequest;
import com.bsc.sso.authentication.logout.Logout;
import com.bsc.sso.authentication.logout.LogoutException;
import com.bsc.sso.authentication.util.ConfigUtil;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

public class CasLogout implements Logout {
    private final SendRequest sendRequest = new SendRequest();

    /**
     * verify and get info user
     *
     * @param request
     * @return
     * @throws LogoutException
     */
    @Override
    public boolean logout(HttpServletRequest request) throws LogoutException {
        try {
            String casLogoutEndpoint = ConfigUtil.getInstance().getProperty("cas.logout.endpoint");
            String callbackSSO = ConfigUtil.getInstance().getProperty("callbackUri");
            String casLogoutUrl = MessageFormat.format(casLogoutEndpoint, callbackSSO);
            LOGGER.info("Cas logout endpoint -------> " + casLogoutUrl);
            HttpResponse response = sendRequest.getRequest(casLogoutUrl);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (Exception e) {
            LOGGER.error("Error when logout with sso type cas cause " + e.getMessage());
            return false;
        }
    }

    private final static Logger LOGGER = Logger.getLogger(CasLogout.class);
}

package com.bsc.sso.authentication.logout.vps;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.http.LogoutRequest;
import com.bsc.sso.authentication.http.SendRequest;
import com.bsc.sso.authentication.logout.Logout;
import com.bsc.sso.authentication.logout.LogoutException;
import com.bsc.sso.authentication.util.ConfigUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class VPSLogout implements Logout {

    @Override
    public boolean logout(HttpServletRequest request) throws LogoutException {
        try {
            String token = null;
            // get token from cookie
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if (name.equals(SSOAuthenticationConstants.TOKEN)) {
                    token = value;
                    break;
                }
            }
            if (token == null) return false;

            String endpoint = ConfigUtil.getInstance().getProperty("vps.logoutUri");
            String username = ConfigUtil.getInstance().getProperty("vps.username");
            String password = ConfigUtil.getInstance().getProperty("vps.password");

            LogoutRequest logoutRequest = new LogoutRequest();
            logoutRequest.setSysUserName(username);
            logoutRequest.setSysPassword(password);

            String message = logoutRequest.toString();

            SendRequest sendRequest = new SendRequest();
            HttpResponse response = sendRequest.sendRequest(message, endpoint, token);

            String responseStr = EntityUtils.toString(response.getEntity());
            LOGGER.info("Response call logout from vps " + responseStr);
            JSONObject payload = new JSONObject(responseStr);
            LOGGER.info("Logout from vps result " + payload.toString());
            // get result
            if (!payload.isNull("StatusCode") && payload.getInt("StatusCode") == 200) {
                LOGGER.info("Logout from vps result " + payload.toString());
                return true;
            }
        } catch (Exception e) {
            LOGGER.info("Error logout vps sso cause " + e + " !!!!!!!!!!!!!!!!!!!!!");
        }
        return false;
    }

    private final static Logger LOGGER = Logger.getLogger(VPSLogout.class);
}

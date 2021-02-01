package com.bsc.sso.authentication.loginurl.vps;

import com.bsc.sso.authentication.http.LoginUrlRequest;
import com.bsc.sso.authentication.http.SendRequest;
import com.bsc.sso.authentication.loginurl.LoginUrl;
import com.bsc.sso.authentication.loginurl.LoginUrlException;
import com.bsc.sso.authentication.util.ConfigUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public class VPSLoginUrl implements LoginUrl {

    @Override
    public String getLoginUrl(HttpServletRequest request) throws LoginUrlException {
        try {
            String endpoint = ConfigUtil.getInstance().getProperty("vps.endpoint");
            String username = ConfigUtil.getInstance().getProperty("vps.username");
            String password = ConfigUtil.getInstance().getProperty("vps.password");
            String param1 = ConfigUtil.getInstance().getProperty("vps.param1");
            String param2 = ConfigUtil.getInstance().getProperty("vps.param2");
            String param3 = ConfigUtil.getInstance().getProperty("vps.param3");
            String returnUri = ConfigUtil.getInstance().getProperty("callbackUri");

            LoginUrlRequest loginUrlRequest = new LoginUrlRequest();
            loginUrlRequest.setSysUserName(username);
            loginUrlRequest.setSysPassword(password);
            loginUrlRequest.setParam1(param1);
            loginUrlRequest.setParam2(param2);
            loginUrlRequest.setParam3(param3);
            loginUrlRequest.setReturnUri(returnUri);

            String message = loginUrlRequest.toString();

            SendRequest sendRequest = new SendRequest();
            HttpResponse response = sendRequest.sendRequest(message, endpoint);
            String responseStr = EntityUtils.toString(response.getEntity());

            JSONObject payload = new JSONObject(responseStr);

            // get result success
            if (payload.getInt("StatusCode") == 200) {
                return payload.getString("Result");
            }
        } catch (Exception e) {
            LOGGER.error("Error get login url from vps sso cause " + e);
            throw new LoginUrlException("Can not get login url");
        }
        return null;
    }

    private final static Logger LOGGER = Logger.getLogger(VPSLoginUrl.class);
}

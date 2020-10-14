package com.bsc.sso.authentication.authen.cas;

import com.bsc.sso.authentication.authen.Authenticate;
import com.bsc.sso.authentication.authen.AuthenticateException;
import com.bsc.sso.authentication.endpoints.oauth2.CallBackEndpoint;
import com.bsc.sso.authentication.http.SendRequest;
import com.bsc.sso.authentication.util.ConfigUtil;
import com.bsc.sso.authentication.xml.XmlUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class CasAuthenticate implements Authenticate {
    private final SendRequest sendRequest = new SendRequest();

    /**
     * verify and get info user
     *
     * @param request
     * @return
     * @throws AuthenticateException
     */
    @Override
    public Map<String, String> verify(HttpServletRequest request) throws AuthenticateException {
        Map<String, String> result = new HashMap<>();
        try {
            String ticket = request.getParameter("ticket");
            if (ticket == null) {
                return result;
            }
            LOGGER.info("Token from cas sso -> " + ticket);
            String casValidateEndpoint = ConfigUtil.getInstance().getProperty("cas.validate.token.endpoint");
            String callbackUrl = ConfigUtil.getInstance().getProperty("callbackUri");
            String casValidateUrl = MessageFormat.format(casValidateEndpoint, ticket, callbackUrl);

            HttpResponse response = sendRequest.getRequest(casValidateUrl);
            String responseStr = EntityUtils.toString(response.getEntity());
            if (responseStr == null) {
                return result;
            }
            String username = XmlUtil.getExtractUserFromCas(responseStr);
            LOGGER.info("Username get from CAS " + username);
            if (!username.equals("")) {
                if (username.contains("@")) {
                    username = username.substring(0, username.indexOf("@"));
                    LOGGER.info("Username get from CAS cut" + username);
                }
                result.put("username", username);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return result;
    }

    private final static Logger LOGGER = Logger.getLogger(CallBackEndpoint.class);
}

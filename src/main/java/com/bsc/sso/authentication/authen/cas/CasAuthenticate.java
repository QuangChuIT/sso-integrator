package com.bsc.sso.authentication.authen.cas;

import com.bsc.sso.authentication.authen.Authenticate;
import com.bsc.sso.authentication.authen.AuthenticateException;
import com.bsc.sso.authentication.http.SendRequest;
import com.bsc.sso.authentication.util.ConfigUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
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
        try {
            String ticket = request.getParameter("ticket");
            if (ticket == null) {
                return null;
            }

            String casValidateEndpoint = ConfigUtil.getInstance().getProperty("cas.validate.token.endpoint");
            String callbackUrl = ConfigUtil.getInstance().getProperty("callbackUri");
            String casValidateUrl = MessageFormat.format(casValidateEndpoint, ticket, callbackUrl);

            HttpResponse response = sendRequest.getRequest(casValidateUrl);
            String responseStr = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

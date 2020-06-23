package com.bsc.sso.authentication.authen.vps;

import com.bsc.sso.authentication.authen.Authenticate;
import com.bsc.sso.authentication.authen.AuthenticateException;
import com.bsc.sso.authentication.http.SendRequest;
import com.bsc.sso.authentication.util.ConfigUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VPSAuthenticate implements Authenticate {
    @Override
    public Map<String, String> verify(HttpServletRequest request) throws AuthenticateException {
        try {
            // get token as param of request
            String token = request.getParameter("token");
            String endpoint = ConfigUtil.getInstance().getProperty("vps.getDataUri");

            // request server to get data
            SendRequest sendRequest = new SendRequest();
            HttpResponse response = sendRequest.getRequest(endpoint, token);
            String responseStr = EntityUtils.toString(response.getEntity());

            JSONObject payload = new JSONObject(responseStr);

            // get result
            if (!payload.isNull("StatusCode") && payload.getInt("StatusCode") == 200) {
                JSONObject obj = payload.getJSONArray("Result").getJSONObject(0);
                Map<String, String> result = toMap(obj);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> toMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            String value = object.get(key).toString();
            map.put(key, value);
        }
        return map;
    }
}

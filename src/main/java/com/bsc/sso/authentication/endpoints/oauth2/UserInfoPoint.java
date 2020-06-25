package com.bsc.sso.authentication.endpoints.oauth2;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.dao.OauthTokenDao;
import com.bsc.sso.authentication.model.OauthState;
import com.bsc.sso.authentication.model.OauthToken;
import com.bsc.sso.authentication.util.CommonUtil;
import com.bsc.sso.authentication.util.MemcacheUtil;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

/**
 * Logout server
 */
@Path("/userinfo")
public class UserInfoPoint {

    private OauthTokenDao oauthTokenDao = new OauthTokenDao();

    /**
     * @param request
     * @return get user info
     * @throws URISyntaxException
     * @throws OAuthSystemException
     */
    @GET
    public Response getUserInfo(@Context HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException {
        // get token from request
        String token = CommonUtil.getHeaderAuthorizationValue(request, CommonUtil.HeaderType.BEARER);
        OauthToken oauthToken = oauthTokenDao.getTokenByAccessToken(token);
        // validate access token
        if (!validateAccessToken(oauthToken)) {
            return buildBadUserInfoResponse();
        }
        // get user info from token
        String username = oauthToken.getUserName();
        Map<String, String> userInfos = (Map<String, String>) MemcacheUtil.getInstance().get(username);
        OAuthASResponse.OAuthTokenResponseBuilder builderResponse = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setParam("sub", username);
        if (userInfos != null && userInfos.size() > 0) {
            for (Map.Entry<String, String> entry : userInfos.entrySet()) {
                builderResponse.setParam(entry.getKey(), entry.getValue());
            }
        }
        // build response
        OAuthResponse response = builderResponse.buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private Response buildBadUserInfoResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError("invalid_token")
                .setErrorDescription("Access token validation failed")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private boolean validateAccessToken(OauthToken oauthToken) {
        // check token exist
        if (oauthToken == null) return false;
        // check token state is active
        if (oauthToken.getState() == null || oauthToken.getState() == OauthState.INACTIVE) return false;
        // check token expire
        Date currentDate = new Date();
        Date timeCreated = oauthToken.getTimeCreated();
        if (timeCreated != null) {
            Date timeExpired = new Date(timeCreated.getTime() + oauthToken.getTokenExpireTime());
            if (currentDate.after(timeExpired)) {
                return false;
            }
        }
        return true;
    }
}

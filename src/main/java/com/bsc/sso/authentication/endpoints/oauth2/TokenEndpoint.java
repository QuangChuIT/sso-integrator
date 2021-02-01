package com.bsc.sso.authentication.endpoints.oauth2;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.dao.OauthCodeDao;
import com.bsc.sso.authentication.dao.OauthConsumerAppDao;
import com.bsc.sso.authentication.dao.OauthTokenDao;
import com.bsc.sso.authentication.model.OauthCode;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;
import com.bsc.sso.authentication.model.OauthToken;
import com.bsc.sso.authentication.token.TokenFactory;
import com.bsc.sso.authentication.util.CommonUtil;
import com.bsc.sso.authentication.util.CookieUtil;
import com.bsc.sso.authentication.validate.OauthConsumerAppValidate;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Class responsible for returning tokens.
 */
@Path("/token")
public class TokenEndpoint {

    private final OauthConsumerAppDao oauthConsumerAppDao = new OauthConsumerAppDao();
    private final OauthCodeDao oauthCodeDao = new OauthCodeDao();
    private final OauthTokenDao oauthTokenDao = new OauthTokenDao();
    private final OauthConsumerAppValidate oauthConsumerAppValidate = new OauthConsumerAppValidate();

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response token(@Context HttpServletRequest request, final MultivaluedMap<String, String> formParams) throws OAuthSystemException {
        try {
            LOGGER.info("Token Endpoint invoke !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! from redirect uri " + formParams.getFirst(OAuth.OAUTH_REDIRECT_URI));
            // validate params
            this.validateFormParams(formParams);
            // get client info
            String[] clientInfos = this.getClientInfo(request);
            // check of client id and client secret
            String clientId = clientInfos[0];
            String clientSecret = clientInfos[1];
            OauthConsumerApp oauthConsumerApp = oauthConsumerAppDao.getByConsumerKey(clientId);
            if (!oauthConsumerAppValidate.checkClientInfo(oauthConsumerApp, clientSecret)) {
                throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT, "Client Authentication failed.");
            }

            String username = "";
            // check for grant types
            if (formParams.getFirst(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                // code - tells the service that we have an authorization code and would like to have token
                String code = formParams.getFirst(OAuth.OAUTH_CODE);
                OauthCode oauthCode = oauthCodeDao.getByCode(code);
                if (!validateAuthCode(oauthCode, oauthConsumerApp)) {
                    LOGGER.error("Code not match with oauth consumer app ------------------->");
                    return buildBadAuthCodeResponse();
                }
                username = oauthCode.getUserName();
                // delete code is used
                oauthCodeDao.deleteOauthCode(code);
            } else if (formParams.getFirst(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
                String refreshToken = formParams.getFirst(OAuth.OAUTH_REFRESH_TOKEN);
                OauthToken oauthToken = oauthTokenDao.getTokenByRefreshToken(refreshToken);
                if (!validateRefreshToken(oauthToken, oauthConsumerApp)) {
                    return buildBadRefreshTokenResponse();
                }
                username = oauthToken.getUserName();
                // delete old token
                oauthTokenDao.deleteToken(oauthToken);
            }
            if (username.contains("@")) {
                username = username.substring(0, username.indexOf("@"));
            }
            LOGGER.warn("username ------------->" + username);
            // get token expire time
            OauthToken oauthToken = buildTokenDto(oauthConsumerApp, username, clientId);
            oauthTokenDao.addToken(oauthToken);
            // send response with token
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setParam("id_token", oauthToken.getToken())
                    .setAccessToken(oauthToken.getAccessToken())
                    .setRefreshToken(oauthToken.getRefreshToken())
                    .setTokenType("Bearer")
                    .setExpiresIn(String.valueOf(oauthToken.getTokenExpireTime() / 1000))
                    .buildJSONMessage();
            return Response.status(response.getResponseStatus()).entity(response.getBody()).build();

        } catch (OAuthProblemException e) {
            OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            return Response.status(res.getResponseStatus()).entity(res.getBody()).build();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error when get token " + e);
            return null;
        }
    }

    private void validateFormParams(final MultivaluedMap<String, String> formParams) throws OAuthProblemException {
        if (!formParams.containsKey(OAuth.OAUTH_GRANT_TYPE)) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "Missing grant_type parameter value");
        }
        // validate by grant type
        if (formParams.getFirst(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
            if (!formParams.containsKey(OAuth.OAUTH_REDIRECT_URI)) {
                throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "Missing redirect_uri parameter value");
            }
            if (!formParams.containsKey(OAuth.OAUTH_CODE)) {
                throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "Missing code parameter value");
            }
        } else if (formParams.getFirst(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
            if (!formParams.containsKey(OAuth.OAUTH_REFRESH_TOKEN)) {
                throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "Missing code parameter value");
            }
        } else {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "Grant type is invalid");
        }
    }

    private String[] getClientInfo(HttpServletRequest request) throws OAuthProblemException {
        String clientInfoStr = CommonUtil.getHeaderAuthorizationValue(request, CommonUtil.HeaderType.BASIC);
        if (clientInfoStr == null) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT, "Client Authentication failed.");
        }
        byte[] byteArray = null;
        try {
            byteArray = Base64.decodeBase64(clientInfoStr.getBytes());
        } catch (Exception e) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT, "Client Authentication failed.");
        }
        String clientInfo = new String(byteArray);
        String[] parses = clientInfo.split(":");
        if (parses.length < 2) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT, "Client Authentication failed.");
        }
        return parses;
    }

    private OauthToken buildTokenDto(OauthConsumerApp oauthConsumerApp, String username, String clientId) throws Exception {
        OauthToken oauthToken = new OauthToken();
        long tokenExpireTime = oauthConsumerApp.getTokenExpireTime();
        long refreshTokenExpireTime = oauthConsumerApp.getRefreshTokenExpireTime();
        if (tokenExpireTime == 0) tokenExpireTime = SSOAuthenticationConstants.TOKEN_PERIOD_DEFAULT;
        if (refreshTokenExpireTime == 0) refreshTokenExpireTime = SSOAuthenticationConstants.TOKEN_PERIOD_DEFAULT;
        oauthToken.setTimeCreated(new Date());
        oauthToken.setTokenExpireTime(tokenExpireTime);
        oauthToken.setRefreshTokenExpireTime(refreshTokenExpireTime);
        oauthToken.setUserName(username);
        oauthToken.setClientId(clientId);
        oauthToken.setState(OauthState.ACTIVE);
        // create and add new token to database for a given user
        final String token = TokenFactory.generateToken(username, tokenExpireTime);
        oauthToken.setToken(token);
        // generate access token and refresh token
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new UUIDValueGenerator());
        String accessToken = oauthIssuerImpl.accessToken();
        String refreshToken = oauthIssuerImpl.refreshToken();
        oauthToken.setAccessToken(accessToken);
        oauthToken.setRefreshToken(refreshToken);
        return oauthToken;
    }

    private Response buildBadAuthCodeResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Invalid authorization code")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private Response buildBadRefreshTokenResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Invalid refresh token")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private boolean validateAuthCode(OauthCode oauthCode, OauthConsumerApp oauthConsumerApp) {
        // check code is exist
        if (oauthCode == null) {
            LOGGER.error("Code is null");
            return false;
        }
        // check code state is active
        if (oauthCode.getState() == null || oauthCode.getState() == OauthState.INACTIVE) {
            LOGGER.error("Code is not active");
            return false;
        }
        // check consumer id
        if (oauthCode.getConsumerId() != oauthConsumerApp.getId()) {
            LOGGER.error("Consumer app with code not match with consumer app !!!!!");
            return false;
        }
        // check code period
        Date currentDate = new Date();
        Date timeCreated = oauthCode.getTimeCreated();
        if (timeCreated != null) {
            Date timeExpired = new Date(timeCreated.getTime() + oauthCode.getValidityPeriod());
            if (currentDate.after(timeExpired)) {
                LOGGER.error("Expired time of code !!!!!");
                return false;
            }
        }
        return true;
    }

    private boolean validateRefreshToken(OauthToken oauthToken, OauthConsumerApp oauthConsumerApp) {
        // check refresh token exist
        if (oauthToken == null) return false;
        // check token state is active
        if (oauthToken.getState() == null || oauthToken.getState() == OauthState.INACTIVE) return false;
        // check consumer id
        if (oauthToken.getConsumerId() != oauthConsumerApp.getId()) return false;
        // check token expire
        Date currentDate = new Date();
        Date timeCreated = oauthToken.getTimeCreated();
        if (timeCreated != null) {
            Date timeExpired = new Date(timeCreated.getTime() + oauthToken.getRefreshTokenExpireTime());
            if (currentDate.after(timeExpired)) {
                return false;
            }
        }
        return true;
    }

    private static final Logger LOGGER = Logger.getLogger(TokenEndpoint.class);
}

package com.bsc.sso.authentication.endpoints.oauth2;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.dao.OauthCodeDao;
import com.bsc.sso.authentication.dao.OauthConsumerAppDao;
import com.bsc.sso.authentication.dao.OauthTokenDao;
import com.bsc.sso.authentication.http.SendRequest;
import com.bsc.sso.authentication.model.OauthCode;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;
import com.bsc.sso.authentication.model.OauthToken;
import com.bsc.sso.authentication.token.TokenFactory;
import com.bsc.sso.authentication.util.CommonUtil;
import com.bsc.sso.authentication.util.ConfigUtil;
import com.bsc.sso.authentication.validate.OauthConsumerAppValidate;
import com.bsc.sso.authentication.xml.XmlUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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

import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.*;

/**
 * Class responsible for returning tokens.
 */
@Path("/token")
public class TokenEndpoint {

    private final OauthConsumerAppDao oauthConsumerAppDao = new OauthConsumerAppDao();
    private final OauthCodeDao oauthCodeDao = new OauthCodeDao();
    private final OauthTokenDao oauthTokenDao = new OauthTokenDao();
    private final OauthConsumerAppValidate oauthConsumerAppValidate = new OauthConsumerAppValidate();
    private final SendRequest sendRequest =new SendRequest();

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response token(@Context HttpServletRequest request, final MultivaluedMap<String, String> formParams) throws OAuthSystemException {
        try {
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
            } else if (formParams.getFirst(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.PASSWORD.toString())) {

                String password = formParams.getFirst(OAuth.OAUTH_PASSWORD);
                String userName= formParams.getFirst(OAuth.OAUTH_USERNAME);

                Map<String, String> body1=new HashMap<>();
                body1.put(OAuth.OAUTH_USERNAME, userName);
                body1.put(OAuth.OAUTH_PASSWORD, password);

                String casEndpointGrantingTicket=ConfigUtil.getInstance().getProperty("cas.endpoint.granting.ticket");
                String[] cas=postApiCAS(casEndpointGrantingTicket,body1);

                if(!cas[0].equals("201"))
                {
                    return buildBadTicketResponse(Integer.parseInt(cas[0]));
                }
                String tgtID=getTGT(cas[1]);
                LOGGER.info("Get TGT_ID Success =========================> "+cas[1]);
                String urlST=casEndpointGrantingTicket+tgtID;
                String casCallbackUrl=ConfigUtil.getInstance().getProperty("callbackUri");
                Map<String, String> body2=new HashMap<>();
                body2.put("service",casCallbackUrl);
                String[] cas2=postApiCAS(urlST,body2);

                if(!cas2[0].equals("200"))
                {
                    return buildBadTicketResponse(Integer.parseInt(cas2[0]));
                }
                LOGGER.info("Get Ticket Success =========================> "+cas2[1]);
                String casValidateEndpoint = ConfigUtil.getInstance().getProperty("cas.endpoint.validate.ticket");
                String casValidateUrl = MessageFormat.format(casValidateEndpoint, casCallbackUrl, cas2[1]);
                String[] userInfo=getApiCAS(casValidateUrl);

                if(!userInfo[0].equals("200"))
                {
                    return buildBadTicketResponse(Integer.parseInt(userInfo[0]));
                }
                username= XmlUtil.getExtractUserFromCas(userInfo[1]);
                // get user name
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
        } else if (formParams.getFirst(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.PASSWORD.toString())) {
            if (!formParams.containsKey(OAuth.OAUTH_PASSWORD)) {
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

    private Response buildBadTicketResponse(int code) throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(code)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Invalid username or password")
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

    public static String[] postApiCAS(String url, Map<String, String> body) throws IOException {

        String[] result = new String[2];
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.addHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> form = new ArrayList<>();
            for (String key : body.keySet()) {
                form.add(new BasicNameValuePair(key, body.get(key)));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
            request.setEntity(entity);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int status = response.getStatusLine().getStatusCode();
                result[0] = String.valueOf(status);
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    result[1] = EntityUtils.toString(httpEntity);
                }

            } catch (Exception e) {
                LOGGER.error(e);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return result;
    }

    public static String[] getApiCAS(String url) throws IOException
    {
        String[] result = new String[2];
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int status = response.getStatusLine().getStatusCode();
                result[0] = String.valueOf(status);
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    result[1] = EntityUtils.toString(httpEntity);
                }

            } catch (Exception e) {
                LOGGER.error(e);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return result;
    }

    public static String getTGT(String str)
    {
        String url=str.substring(str.indexOf("=")+2, str.indexOf("method")-2);
        return url.substring(url.lastIndexOf("/"), url.length());
    }



    private static final Logger LOGGER = Logger.getLogger(TokenEndpoint.class);
}

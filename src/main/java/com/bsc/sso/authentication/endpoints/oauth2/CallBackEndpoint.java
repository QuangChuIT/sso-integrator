package com.bsc.sso.authentication.endpoints.oauth2;

import com.bsc.sso.authentication.OauthSessionManager;
import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.authen.Authenticate;
import com.bsc.sso.authentication.authen.AuthenticateFactory;
import com.bsc.sso.authentication.authen.bkav.BkavAuthenticate;
import com.bsc.sso.authentication.dao.OauthCodeDao;
import com.bsc.sso.authentication.dao.OauthConsumerAppDao;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.soap.SsoCheckResponse;
import com.bsc.sso.authentication.soap.WebService;
import com.bsc.sso.authentication.soap.WebServiceLocator;
import com.bsc.sso.authentication.soap.WebServiceSoap_PortType;
import com.bsc.sso.authentication.util.CommonUtil;
import com.bsc.sso.authentication.util.ConfigUtil;
import com.bsc.sso.authentication.util.MemcacheUtil;
import com.bsc.sso.authentication.validate.OauthConsumerAppValidate;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;

import javax.servlet.http.Cookie;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service authorization (grant permission) page, to authorize app on behalf of a user.
 * It doesn't authenticate user (password).
 */
@Path("/callback")
public class CallBackEndpoint {

    private OauthConsumerAppDao consumerAppDao = new OauthConsumerAppDao();
    private OauthCodeDao oauthCodeDao = new OauthCodeDao();
    private OauthConsumerAppValidate oauthConsumerAppValidate = new OauthConsumerAppValidate();
    private AuthenticateFactory authenticateFactory = new AuthenticateFactory();

    /**
     * @param request
     * @return Authorization code via the redirect back to the app.
     * @throws URISyntaxException
     * @throws OAuthSystemException
     */
    @GET
    public Response authorize(@Context HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException {
        try {

            Map<String, String> userInfos = authenticateFactory.authentication(request, "vps");

            // set params from cookie
            CommonUtil.setParamsAsCookie(request);

            // check account
            if (userInfos == null || userInfos.size() == 0 || !userInfos.containsKey("UserName")) {
                throw OAuthProblemException.error("Authorize is invalid!");
            }
            String account = userInfos.get("UserName");
            // save info user to cache
            userInfos.remove("UserName");
            MemcacheUtil.getInstance().set(account, userInfos);

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);

            // validate request
            this.validate(request);

            //Build response and redirect to given in the request URI
            String redirectURI = (String) request.getAttribute(OAuth.OAUTH_REDIRECT_URI);

            // generate auth code
            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new UUIDValueGenerator());
            final String authorizationCode = oauthIssuerImpl.authorizationCode();
            String clientId = (String) request.getAttribute(OAuth.OAUTH_CLIENT_ID);
            oauthCodeDao.addAuthCode(authorizationCode, clientId, account);
            builder.setCode(authorizationCode);
            String session_state = OauthSessionManager.getSessionStateParam(clientId, redirectURI, account);
            builder.setParam("session_state", session_state);

            final OAuthResponse oAuthResponse = builder.location(redirectURI).buildQueryMessage();
            URI url = new URI(oAuthResponse.getLocationUri());

            // login successful, generate cookie
            int cookieExpireTime = Integer.parseInt(ConfigUtil.getInstance().getProperty(SSOAuthenticationConstants.COOKIE_EXPIRE_TIME));
            String cookieValue = CommonUtil.generateCookie(account, cookieExpireTime);
            NewCookie oauthCookie = new NewCookie(SSOAuthenticationConstants.OAUTH_SSO_COOKIE_NAME, cookieValue, "/", null, null, cookieExpireTime, false);

            // save token of vps to cookie
            String vpsToken = request.getParameter("token");
            NewCookie tokenCookie = new NewCookie(SSOAuthenticationConstants.TOKEN, vpsToken, "/", null, null, cookieExpireTime, false);


            //Send response to given URI
            Response response = Response.status(oAuthResponse.getResponseStatus()).location(url).cookie(oauthCookie).cookie(tokenCookie).build();
            return response;

        } catch (OAuthProblemException e) {
            log.error(e);
            final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);
            String redirectUri = ConfigUtil.getInstance().getProperty("errorUri");
            final OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .error(e).location(redirectUri).buildQueryMessage();
            final URI location = new URI(response.getLocationUri());
            return responseBuilder.location(location).build();
        }
    }

    /**
     * validate request
     *
     * @param request
     * @throws OAuthProblemException
     */
    private void validate(HttpServletRequest request) throws OAuthProblemException {
        // validate client id
        String clientId = (String) request.getAttribute(OAuth.OAUTH_CLIENT_ID);
        OauthConsumerApp oauthConsumerApp = consumerAppDao.getByConsumerKey(clientId);
        if (!oauthConsumerAppValidate.checkClientId(oauthConsumerApp)) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT, "Client Id is invalid!");
        }
        // validate oauth code
        String responseType = (String) request.getAttribute(OAuth.OAUTH_RESPONSE_TYPE);
        if (!responseType.equals(ResponseType.CODE.toString())) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "Authorize is only support authz by code!");
        }
        // validate call_back_url
        String redirectUri = (String) request.getAttribute(OAuth.OAUTH_REDIRECT_URI);
        if (!oauthConsumerAppValidate.checkCallbackUrl(oauthConsumerApp, redirectUri)) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "Call Back URL is invalid!");
        }
    }


    private final Logger log = Logger.getLogger(CallBackEndpoint.class);
}

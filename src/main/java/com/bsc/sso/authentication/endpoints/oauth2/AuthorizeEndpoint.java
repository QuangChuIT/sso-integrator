package com.bsc.sso.authentication.endpoints.oauth2;

import com.bsc.sso.authentication.OauthSessionManager;
import com.bsc.sso.authentication.dao.OauthCodeDao;
import com.bsc.sso.authentication.dao.OauthConsumerAppDao;
import com.bsc.sso.authentication.loginurl.LoginUrlFactory;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.util.CommonUtil;
import com.bsc.sso.authentication.validate.CookieValidate;
import com.bsc.sso.authentication.validate.OauthConsumerAppValidate;
import org.apache.log4j.Logger;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Service authorization (grant permission) page, to authorize app on behalf of a user.
 * It doesn't authenticate user (password).
 */
@Path("/authorize")
public class AuthorizeEndpoint {

    private final OauthCodeDao oauthCodeDao = new OauthCodeDao();
    private final OauthConsumerAppDao consumerAppDao = new OauthConsumerAppDao();
    private final OauthConsumerAppValidate oauthConsumerAppValidate = new OauthConsumerAppValidate();
    private final CookieValidate cookieValidate = new CookieValidate();
    private final LoginUrlFactory loginUrlFactory = new LoginUrlFactory();

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
            String type = request.getParameter("type");

            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

            // validate request
            this.validate(oauthRequest);

            //create response for authorize endpoint
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);

            // validate cookie, if valid, don't need redirect server url, then generate code and return
            String username = cookieValidate.authenticateCookie(request);
            if (username != null) {
                return generateAuthCode(oauthRequest, builder, username);
            }

            // Build response and redirect to given in the request URI
            // String redirectURI = ConfigUtil.getInstance().getProperty(SSOAuthenticationConstants.CONNECT_SSO_SERVER_URL) + "?ReturnURL=" + getFullURLSSORedirect(request);
            String redirectURI = loginUrlFactory.getLoginUrl(request, type);
            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
            URI url = new URI(response.getLocationUri());
            // save params before redirect to cookie
            NewCookie[] cookies = CommonUtil.getCookieAsParams(request);

            // Send response to given URI
            return Response.status(response.getResponseStatus()).location(url).cookie(cookies).build();
        } catch (OAuthProblemException e) {
            LOGGER.error(e);
            return CommonUtil.buildErrorResponse(e);
        }
    }

    /**
     * validate request
     *
     * @param oauthRequest
     * @throws OAuthProblemException
     */
    private void validate(OAuthAuthzRequest oauthRequest) throws OAuthProblemException {
        // validate client id
        String clientId = oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID);
        OauthConsumerApp oauthConsumerApp = consumerAppDao.getByConsumerKey(clientId);
        if (!oauthConsumerAppValidate.checkClientId(oauthConsumerApp)) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT, "Client Id is invalid!");
        }
        // validate oauth code
        String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
        if (!responseType.equals(ResponseType.CODE.toString())) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "Authorize is only support authorization by code!");
        }
        // validate call_back_url
        String redirectUri = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
        if (!oauthConsumerAppValidate.checkCallbackUrl(oauthConsumerApp, redirectUri)) {
            throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "Call Back URL is invalid!");
        }
    }

    /**
     * generate auth code
     *
     * @param oauthRequest
     * @param builder
     * @param username
     * @return
     * @throws URISyntaxException
     * @throws OAuthSystemException
     */
    private Response generateAuthCode(OAuthAuthzRequest oauthRequest, OAuthASResponse.OAuthAuthorizationResponseBuilder builder, String username)
            throws URISyntaxException, OAuthSystemException {

        // generate auth code
        OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new UUIDValueGenerator());
        final String authorizationCode = oauthIssuerImpl.authorizationCode();
        String clientId = oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID);
        //Build response and redirect to given in the request URI
        String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
        oauthCodeDao.addAuthCode(authorizationCode, clientId, username);
        builder.setCode(authorizationCode);
        String session_state = OauthSessionManager.getSessionStateParam(clientId, redirectURI, username);
        builder.setParam("session_state", session_state);

        final OAuthResponse oAuthResponse = builder.location(redirectURI).buildQueryMessage();
        URI url = new URI(oAuthResponse.getLocationUri());

        return Response.status(oAuthResponse.getResponseStatus()).location(url).build();
    }

    private final static Logger LOGGER = Logger.getLogger(AuthorizeEndpoint.class);
}

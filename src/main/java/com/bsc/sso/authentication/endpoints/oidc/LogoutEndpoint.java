package com.bsc.sso.authentication.endpoints.oidc;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.dao.OauthTokenDao;
import com.bsc.sso.authentication.logout.LogoutFactory;
import com.bsc.sso.authentication.model.OauthToken;
import com.bsc.sso.authentication.token.TokenUtil;
import com.bsc.sso.authentication.util.CommonUtil;
import com.bsc.sso.authentication.util.ConfigUtil;
import com.bsc.sso.authentication.util.FileUtil;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
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
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Logout server
 */
@Path("/logout")
public class LogoutEndpoint {

    private LogoutFactory logoutFactory = new LogoutFactory();
    private OauthTokenDao oauthTokenDao = new OauthTokenDao();

    /**
     * @param request
     * @return Authorization code via the redirect back to the app.
     * @throws URISyntaxException
     * @throws OAuthSystemException
     */
    @GET
    public Response logout(@Context HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException {
        String redirectURI = request.getParameter(SSOAuthenticationConstants.OAUTH_LOGOUT_REDIRECT_URI);
        if (OAuthUtils.isEmpty(redirectURI)) {
            return this.buildErrorResponse("OAuth callback url needs to be provided by client.");
        }

        // verify token
        String tokenId = request.getParameter(SSOAuthenticationConstants.OAUTH_ID_TOKEN_HINT);
        boolean verifyToken = verifyToken(tokenId);
        if(!verifyToken) {
            return this.buildErrorResponse("Token is invalid.");
        }

        boolean logout = logoutFactory.logout(request, "vps");

        if (!logout) {
            return this.buildErrorResponse("Can not logout.");
        }

        // delete token in database
        OauthToken token = oauthTokenDao.getTokenByTokenId(tokenId);
        oauthTokenDao.deleteToken(token);

        // reponse to redirect uri
        OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
        final OAuthResponse oAuthResponse = builder.location(redirectURI).buildQueryMessage();
        URI url = new URI(oAuthResponse.getLocationUri());

        // delete all cookie
        NewCookie[] cookies = CommonUtil.deleteAllCookie(request);

        Response response = Response.status(oAuthResponse.getResponseStatus()).location(url).cookie(cookies).build();
        return response;
    }

    private Response buildErrorResponse(String description) throws URISyntaxException, OAuthSystemException {
        final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);
        OAuthProblemException e = OAuthProblemException.error("Logout error", description);
        String redirectUri = ConfigUtil.getInstance().getProperty("errorUri");
        final OAuthResponse response =
                OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                        .error(e).location(redirectUri).buildQueryMessage();
        final URI location = new URI(response.getLocationUri());
        return responseBuilder.location(location).build();
    }

    private boolean verifyToken(String idToken) {
        try {
            String pemKey = FileUtil.getStringFromResource("public.pem");
            RSAPublicKey publicKey = (RSAPublicKey)TokenUtil.toRSAKey(pemKey);

            SignedJWT signedJWT = SignedJWT.parse(idToken);
            JWSVerifier verifier = new RSASSAVerifier(publicKey);

            return signedJWT.verify(verifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

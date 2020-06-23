package com.bsc.sso.authentication.endpoints;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.logout.LogoutFactory;
import com.bsc.sso.authentication.util.CommonUtil;
import com.bsc.sso.authentication.util.ConfigUtil;
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

/**
 * Logout server
 */
@Path("/logout")
public class LogoutEndpoint {

    private LogoutFactory logoutFactory = new LogoutFactory();

    /**
     * @param request
     * @return Authorization code via the redirect back to the app.
     * @throws URISyntaxException
     * @throws OAuthSystemException
     */
    @GET
    public Response logout(@Context HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException {
        String redirectURI = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
        if (OAuthUtils.isEmpty(redirectURI)) {
            OAuthProblemException e = OAuthProblemException.error("Logout error", "OAuth callback url needs to be provided by client.");
            final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);
            String redirectUri = ConfigUtil.getInstance().getProperty("errorUri");
            final OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .error(e).location(redirectUri).buildQueryMessage();
            final URI location = new URI(response.getLocationUri());
            return responseBuilder.location(location).build();
        }

        boolean logout = logoutFactory.logout(request, "vps");

        if (!logout) {
            OAuthProblemException e = OAuthProblemException.error("Logout error", "Can not logout.");
            final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);
            String redirectUri = ConfigUtil.getInstance().getProperty("errorUri");
            final OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .error(e).location(redirectUri).buildQueryMessage();
            final URI location = new URI(response.getLocationUri());
            return responseBuilder.location(location).build();
        }

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
}

package com.bsc.sso.authentication.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.token.TokenUtil;
import com.bsc.sso.authentication.token.object.Token;
import com.bsc.sso.authentication.token.object.TokenHeader;
import com.bsc.sso.authentication.token.object.TokenPayload;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

public class CommonUtil {

    public enum HeaderType {
        BASIC, BEARER
    }

    /**
     * get full URL SSO to redirect
     *
     * @param request
     * @return
     */
    public static String getFullURLSSORedirect(HttpServletRequest request) {
        return CommonUtil.getContextPath(request) + SSOAuthenticationConstants.CALLBACK_URL;
    }

    public static String getContextPath(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String contextPath = url.substring(0, url.length() - uri.length() + ctx.length());
        return contextPath;
    }

    public static String generateCookie(String username, int timeExpire) {
        try {
            TokenPayload tokenPayload = new TokenPayload();
            TokenHeader tokenHeader = new TokenHeader();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, timeExpire);

            tokenPayload.setUsername(username);
            tokenPayload.setExp(calendar.getTimeInMillis());

            String pemKey = FileUtil.getStringFromResource("private.pem");
            Algorithm alg = TokenUtil.getAlgWithRSA256(pemKey);

            Token token = new Token(alg, tokenHeader, tokenPayload);
            return token.getToken();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NewCookie[] getCookieAsParams(HttpServletRequest request) {
        NewCookie[] result = new NewCookie[4];
        String clientId = request.getParameter(OAuth.OAUTH_CLIENT_ID);
        String responseType = request.getParameter(OAuth.OAUTH_RESPONSE_TYPE);
        String redirectUri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
        String type = request.getParameter(SSOAuthenticationConstants.SSO_INTEGRATOR_TYPE_PARAM);
//        String domainName = request.getServerName();
//        String domainNamePrefix = domainName.substring(domainName.indexOf("."), domainName.length());
        result[0] = new NewCookie(SSOAuthenticationConstants.CLIENT_ID_COOKIE, clientId, "/", null, null, Integer.MAX_VALUE, false);
        result[1] = new NewCookie(SSOAuthenticationConstants.RESPONSE_TYPE_COOKIE, responseType, "/", null, null, Integer.MAX_VALUE, false);
        result[2] = new NewCookie(SSOAuthenticationConstants.REDIRECT_URI_COOKIE, redirectUri, "/", null, null, Integer.MAX_VALUE, false);
        result[3] = new NewCookie(SSOAuthenticationConstants.SSO_INTEGRATOR_TYPE_COOKIE, type, "/", null, null, Integer.MAX_VALUE, false);
        return result;
    }

    public static NewCookie[] deleteAllCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        NewCookie[] newCookies = new NewCookie[cookies.length];
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            NewCookie cookie = new NewCookie(name, "", "/", null, null, 0, false);
            newCookies[i] = cookie;
        }

        return newCookies;
    }

    public static void setParamsAsCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();
            if (name.equals(SSOAuthenticationConstants.CLIENT_ID_COOKIE)
                    || name.equals(SSOAuthenticationConstants.RESPONSE_TYPE_COOKIE)
                    || name.equals(SSOAuthenticationConstants.REDIRECT_URI_COOKIE)) {
                request.setAttribute(name, value);
            }
        }

    }

    public static String getHeaderAuthorizationValue(HttpServletRequest request, HeaderType type) {
        String authTokenHeader = request.getHeader("Authorization");
        String headerPrefix = null;
        if (type.equals(HeaderType.BASIC)) {
            headerPrefix = "Basic ";
        } else if (type.equals(HeaderType.BEARER)) {
            headerPrefix = "Bearer ";
        }
        if (authTokenHeader != null && headerPrefix != null && authTokenHeader.startsWith(headerPrefix)) {
            return authTokenHeader.substring(headerPrefix.length());
        }
        return null;
    }

    public static Response buildErrorResponse(OAuthProblemException e) throws OAuthSystemException, URISyntaxException {
        final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);
        String redirectUri = ConfigUtil.getInstance().getProperty("errorUri");
        final OAuthResponse response =
                OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                        .error(e).location(redirectUri).buildQueryMessage();
        final URI location = new URI(response.getLocationUri());
        return responseBuilder.location(location).build();
    }
}

package com.bsc.sso.authentication.validate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.token.TokenUtil;
import com.bsc.sso.authentication.util.FileUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class CookieValidate {
    public String authenticateCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        String ssoCookie = null;
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            if (name.equals(SSOAuthenticationConstants.OAUTH_SSO_COOKIE_NAME)) {
                ssoCookie = cookies[i].getValue();
                break;
            }
        }
        if (ssoCookie == null) return null;
        // verify cookie
        String pemKey = null;
        try {
            pemKey = FileUtil.getStringFromResource("public.pem");
            Algorithm alg = TokenUtil.getAlgWithRSA256(pemKey);
            JWTVerifier verifier = JWT.require(alg)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(ssoCookie);
            Claim claim = jwt.getClaims().get("username");
            String username = claim.asString();
            Date date = jwt.getExpiresAt();
            if (date.before(new Date())) {
                // cookie expire
                return null;
            }
            return username;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

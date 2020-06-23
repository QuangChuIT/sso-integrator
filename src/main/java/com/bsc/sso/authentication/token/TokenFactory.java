package com.bsc.sso.authentication.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.bsc.sso.authentication.token.object.Token;
import com.bsc.sso.authentication.token.object.TokenHeader;
import com.bsc.sso.authentication.token.object.TokenPayload;
import com.bsc.sso.authentication.util.FileUtil;
import com.bsc.sso.authentication.util.MemcacheUtil;

import java.util.Calendar;
import java.util.Map;

public class TokenFactory {

    private static final String ALG_RS512 = "RS512";
    private static final String TOKEN_TYPE = "jwt";

    public static String generateToken(String username, long timeExpire) throws Exception {
        TokenHeader tokenHeader = new TokenHeader();
        TokenPayload tokenPayload = new TokenPayload();

        Calendar calendar = Calendar.getInstance();

        tokenHeader.setAlg(ALG_RS512);
        tokenHeader.setType(TOKEN_TYPE);

//        tokenPayload.setUsername(username);
        tokenPayload.setIat(calendar.getTimeInMillis());
        tokenPayload.setExp(calendar.getTimeInMillis() + timeExpire);

        // get claims of user
        Map<String, String> claims = (Map<String, String>) MemcacheUtil.getInstance().get(username);
        // add username with name 'sub'
        claims.put("sub", username);
        claims.put("aud", "sso-integrator");
        tokenPayload.setClaims(claims);

        String pemKey = FileUtil.getStringFromResource("private.pem");
        Algorithm alg = TokenUtil.getAlgWithRSA512(pemKey);

        Token token = new Token(alg, tokenHeader, tokenPayload);
        System.out.println(token.getToken());
        return token.getToken();
    }
}

package com.bsc.sso.authentication.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.bsc.sso.authentication.token.object.TokenHeader;
import com.bsc.sso.authentication.token.object.TokenPayload;

import java.util.Date;
import java.util.Map;

public class TokenBuilder {
    private TokenHeader tokenHeader;
    private TokenPayload tokenPayload;

    public TokenBuilder() {
    }

    public TokenBuilder withTokenHeader(TokenHeader tokenHeader) {
        this.tokenHeader = tokenHeader;
        return this;
    }

    public TokenBuilder withTokenPayload(TokenPayload tokenPayload) {
        this.tokenPayload = tokenPayload;
        return this;
    }

    public String build(Algorithm algorithm) throws TokenException {
        try {
            if (this.tokenHeader == null) {
                throw new TokenException("Require tokenPayload is not null");
            } else if (this.tokenPayload == null) {
                throw new TokenException("Require tokenPayload is not null");
            } else {
                Builder builder = JWT.create();
                builder.withHeader(this.tokenHeader.toMapHeader()).withExpiresAt(new Date(this.tokenPayload.getExp())).withIssuedAt(new Date(this.tokenPayload.getIat())).withClaim("username", this.tokenPayload.getUsername());
                Map<String, String> claims = tokenPayload.getClaims();
                if (claims != null && claims.size() > 0) {
                    for (Map.Entry<String, String> entry : claims.entrySet()) {
                        builder.withClaim(entry.getKey(), entry.getValue());
                    }
                }

                return builder.sign(algorithm);
            }
        } catch (Exception e) {
            throw new TokenException(e);
        }
    }
}

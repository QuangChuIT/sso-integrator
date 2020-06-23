package com.bsc.sso.authentication.token.object;

import com.auth0.jwt.algorithms.Algorithm;
import com.bsc.sso.authentication.token.TokenBuilder;
import com.bsc.sso.authentication.token.TokenException;

public class Token {
    private Algorithm algorithm;
    private TokenHeader tokenHeader;
    private TokenPayload tokenPayload;

    public Token(Algorithm algorithm, TokenHeader tokenHeader, TokenPayload tokenPayload) {
        this.algorithm = algorithm;
        this.tokenHeader = tokenHeader;
        this.tokenPayload = tokenPayload;
    }

    public Algorithm getAlgorithm() {
        return this.algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public TokenHeader getTokenHeader() {
        return this.tokenHeader;
    }

    public void setTokenHeader(TokenHeader tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public TokenPayload getTokenPayload() {
        return this.tokenPayload;
    }

    public void setTokenPayload(TokenPayload tokenPayload) {
        this.tokenPayload = tokenPayload;
    }

    public String getToken() throws TokenException {
        return (new TokenBuilder()).withTokenHeader(this.tokenHeader).withTokenPayload(this.tokenPayload).build(this.algorithm);
    }
}

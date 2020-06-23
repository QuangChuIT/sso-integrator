package com.bsc.sso.authentication.token.object;

import java.util.Map;

public class TokenPayload {
    protected String username;
    protected long iat;
    protected long exp;

    protected Map<String, String> claims;

    public TokenPayload() {
    }

    public TokenPayload(String username, long iat, long exp, Map<String, String> claims) {
        this.username = username;
        this.iat = iat;
        this.exp = exp;
        this.claims = claims;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getIat() {
        return this.iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public long getExp() {
        return this.exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public Map<String, String> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, String> claims) {
        this.claims = claims;
    }

}
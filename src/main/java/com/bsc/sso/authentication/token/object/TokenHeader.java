package com.bsc.sso.authentication.token.object;

import java.util.HashMap;
import java.util.Map;

public class TokenHeader {
    protected String alg;
    protected String type;

    public TokenHeader() {
    }

    public TokenHeader(String alg, String type) {
        this.alg = alg;
        this.type = type;
    }

    public String getAlg() {
        return this.alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> toMapHeader() {
        Map<String, Object> map = new HashMap<>();
        map.put("alg", this.alg);
        map.put("type", this.type);

        return map;
    }
}

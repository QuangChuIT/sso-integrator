package com.bsc.sso.authentication.client.constants;

public class Constants {

    private Constants() {
    }

    //Constants used for OAuth Secret Revoke and Regeneration
    public static final String OAUTH_APP_NEW_STATE = "new_state";
    public static final String OAUTH_APP_NEW_SECRET_KEY = "new_secretKey";
    public static final String ACTION_PROPERTY_KEY = "action";
    public static final String ACTION_REVOKE = "revoke";
    public static final String ACTION_REGENERATE = "regenerate";

}

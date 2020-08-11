package com.bsc.sso.authentication;

public class SSOAuthenticationConstants {
    public static final String CONNECT_SSO_SERVER_URL = "connectServerUrl";
    public static final String CALLBACK_URL = "/oauth2/callback";
    public static final String ERROR_URL = "/oauth2/error";
    public static final String CONNECT_SSO_COOKIE_NAME = "connectCookieName";
    public static final long TOKEN_PERIOD_DEFAULT = 86400000;
    public static final String OAUTH_SSO_COOKIE_NAME = "OauthSSO";
    public static final String TOKEN = "Token";
    public static final String COOKIE_EXPIRE_TIME = "cookieExpireTime"; // seconds
    public static final String CLIENT_ID_COOKIE = "client_id";
    public static final String RESPONSE_TYPE_COOKIE = "response_type";
    public static final String REDIRECT_URI_COOKIE = "redirect_uri";
    //Constants used for OAuth Connect Configuration UI
    public static final String CALLBACK_URL_REGEXP_PREFIX = "regexp=";
    public static final String OAUTH_LOGOUT_REDIRECT_URI = "post_logout_redirect_uri";
    public static final String OAUTH_ID_TOKEN_HINT = "id_token_hint";
    public static final String SSO_INTEGRATOR_TYPE_PARAM = "type";
    public static final String SSO_INTEGRATOR_TYPE_COOKIE = "SSOType";
}

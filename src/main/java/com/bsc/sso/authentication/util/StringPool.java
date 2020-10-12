package com.bsc.sso.authentication.util;

public class StringPool {
    public static String GET_CONSUMER_APP_BY_CLIENT_ID = "select id, consumer_key, consumer_secret, app_name, app_state, callback_url, token_expire_time, refresh_token_expire_time from oauth2_consumer_apps where consumer_key=?";

    public static String ADD_AUTH_CODE_QUERY = "insert into oauth2_authorization_code (code, consumer_id, username, state, time_created, validity_period) values (?, ?, ?, ?, ?, ?)";

    public static String GET_BY_CODE ="select id, code, consumer_id, username, state, time_created, validity_period from oauth2_authorization_code where code=?";
}

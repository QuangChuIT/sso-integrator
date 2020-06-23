package com.bsc.sso.authentication.validate;

import com.bsc.sso.authentication.SSOAuthenticationConstants;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;

public class OauthConsumerAppValidate {

    public boolean checkClientId(OauthConsumerApp oauthConsumerApp) {
        if (oauthConsumerApp == null || oauthConsumerApp.getAppState() == null || oauthConsumerApp.getAppState() == OauthState.INACTIVE)
            return false;
        return true;
    }

    public boolean checkCallbackUrl(OauthConsumerApp oauthConsumerApp, String callbackURI) {
        if (oauthConsumerApp == null || oauthConsumerApp.getCallbackUrl() == null) return false;
        String regexp = null;
        String registeredCallbackUrl = oauthConsumerApp.getCallbackUrl();
        if (registeredCallbackUrl.startsWith(SSOAuthenticationConstants.CALLBACK_URL_REGEXP_PREFIX)) {
            regexp = registeredCallbackUrl.substring(SSOAuthenticationConstants.CALLBACK_URL_REGEXP_PREFIX.length());
        }

        if (regexp != null && callbackURI.matches(regexp)) {
            return true;
        } else if (registeredCallbackUrl.equals(callbackURI)) {
            return true;
        }
        return false;
    }

    public boolean checkClientInfo(OauthConsumerApp oauthConsumerApp, String clientSecret) {
        // check client id
        if (oauthConsumerApp == null || oauthConsumerApp.getAppState() == null || oauthConsumerApp.getAppState() == OauthState.INACTIVE)
            return false;
        // check client secret
        if (oauthConsumerApp.getConsumerSecret() == null || !oauthConsumerApp.getConsumerSecret().equals(clientSecret))
            return false;
        return true;
    }
}

package com.bsc.sso.authentication.client.application;

import com.bsc.sso.authentication.client.constants.Constants;
import com.bsc.sso.authentication.dao.OauthCodeDao;
import com.bsc.sso.authentication.dao.OauthConsumerAppDao;
import com.bsc.sso.authentication.dao.OauthTokenDao;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;
import com.bsc.sso.authentication.model.OauthToken;
import com.bsc.sso.authentication.util.OAuthUtil;

import java.util.Properties;
import java.util.Set;

public class ServiceClientImpl {
    OauthConsumerAppDao appDAO;
    OauthTokenDao tokenDao;
    OauthCodeDao codeDao;

    public ServiceClientImpl() throws Exception {
        appDAO = new OauthConsumerAppDao();
        tokenDao = new OauthTokenDao();
        codeDao = new OauthCodeDao();
    }

    public OauthConsumerApp[] getAllConsumerApp() throws Exception {
        return appDAO.getAllConsumerApp();
    }

    public OauthConsumerApp getConsumerApp(String appName) throws Exception {
        return appDAO.getConsumerApp(appName);
    }

    public void registerApplicationData(OauthConsumerApp oauthConsumerApp) throws Exception {
        oauthConsumerApp.setAppState(OauthState.valueOf(OauthState.ACTIVE.name()));
        oauthConsumerApp.setConsumerKey(OAuthUtil.getRandomNumber());
        oauthConsumerApp.setConsumerSecret(OAuthUtil.getRandomNumber());
        appDAO.createOrUpdate(oauthConsumerApp);
    }

    public void updateConsumerApp(OauthConsumerApp oauthConsumerApp) throws Exception {
        appDAO.update(oauthConsumerApp);
    }

    public void deleteConsumerApp(OauthConsumerApp oauthConsumerApp) throws Exception {
        appDAO.delete(oauthConsumerApp);
    }

    public String getOauthApplicationState(String consumerkey) throws Exception {
        return appDAO.getOauthApplicationState(consumerkey);
    }

    public void updateAndRetrieveOauthSecretKey(String consumerKey) throws Exception {

        Properties properties = new Properties();
        properties.setProperty(Constants.OAUTH_APP_NEW_SECRET_KEY, OAuthUtil.getRandomNumber());
        properties.setProperty(Constants.ACTION_PROPERTY_KEY, Constants.ACTION_REGENERATE);

        updateAppAndRevokeTokensAndAuthzCodes(consumerKey, properties);
    }

    public void updateOauthApplicationState(String consumerKey, String newState) throws Exception {
        Properties properties = new Properties();
        properties.setProperty(Constants.OAUTH_APP_NEW_STATE, newState);
        properties.setProperty(Constants.ACTION_PROPERTY_KEY, Constants.ACTION_REVOKE);

        updateAppAndRevokeTokensAndAuthzCodes(consumerKey, properties);
    }

    void updateAppAndRevokeTokensAndAuthzCodes(String consumerKey,
                                               Properties properties) throws Exception {

        int countToken = 0;
        String[] accessTokens = null;
        OauthToken[] activeDetailedTokens = tokenDao.getActiveAcessTokenDataByConsumerKey(consumerKey);
        if (activeDetailedTokens != null && activeDetailedTokens.length > 0) {
            accessTokens = new String[activeDetailedTokens.length];

            for (OauthToken detailToken : activeDetailedTokens) {
                String token = detailToken.getAccessToken();
                accessTokens[countToken] = token;
                countToken++;
            }
        }


        String[] authorizationCodes = codeDao.getActiveAuthorizationCodesByConsumerKey(consumerKey);

        appDAO.updateAppAndRevokeTokensAndAuthzCodes(consumerKey, properties, authorizationCodes, accessTokens);
    }
}

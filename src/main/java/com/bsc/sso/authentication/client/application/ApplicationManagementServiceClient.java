package com.bsc.sso.authentication.client.application;

import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationManagementServiceClient {
    ServiceClientImpl stub;
    private static final Log log = LogFactory.getLog(ApplicationManagementServiceClient.class);

    public ApplicationManagementServiceClient() throws Exception {
        stub = new ServiceClientImpl();
    }

    public OauthConsumerApp[] getAllApplicationBasicInfo() throws Exception {
        return stub.getAllConsumerApp();
    }

    public OauthConsumerApp getConsumerApp(String appName) throws Exception {
        return stub.getConsumerApp(appName);
    }

    public void registerApplicationData(OauthConsumerApp oauthConsumerApp) throws Exception {
        stub.registerApplicationData(oauthConsumerApp);
    }

    public void updateApplicationData(OauthConsumerApp oauthConsumerApp) throws Exception {
        stub.updateConsumerApp(oauthConsumerApp);
    }

    public void deleteApplication(OauthConsumerApp oauthConsumerApp) throws Exception {
        stub.deleteConsumerApp(oauthConsumerApp);
    }

    public void regenerateAndRetrieveOauthSecretKey(String spName, String consumerkey) throws Exception {
        stub.updateAndRetrieveOauthSecretKey(consumerkey);
    }

    public String getOauthApplicationState(String consumerKey) throws Exception {
        return stub.getOauthApplicationState(consumerKey);
    }

    public void updateOauthApplicationState(String consumerKey, String appState) throws Exception {
        stub.updateOauthApplicationState(consumerKey, appState);
    }
}

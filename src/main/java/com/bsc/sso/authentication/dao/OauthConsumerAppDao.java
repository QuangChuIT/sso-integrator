package com.bsc.sso.authentication.dao;

import com.bsc.sso.authentication.client.constants.Constants;
import com.bsc.sso.authentication.model.OauthCode;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;
import com.bsc.sso.authentication.model.OauthToken;
import com.bsc.sso.authentication.util.MemcacheUtil;
import com.bsc.sso.authentication.util.SSODatabaseUtil;
import com.bsc.sso.authentication.util.StringPool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

public class OauthConsumerAppDao {
    private static final Logger log = Logger.getLogger(OauthConsumerAppDao.class);

    public OauthConsumerAppDao() {
    }

    public OauthConsumerApp getByConsumerKey(String consumerKey) {
        OauthConsumerApp oauthApp = null;
        // get from cached
        oauthApp = (OauthConsumerApp) MemcacheUtil.getInstance().get(consumerKey);
        Connection connection = SSODatabaseUtil.getDBConnection();
        if (oauthApp != null) return oauthApp;
        // get from database
        PreparedStatement prepStmt = null;
        try {
            if (connection != null) {
                prepStmt = connection.prepareStatement(StringPool.GET_CONSUMER_APP_BY_CLIENT_ID);
                prepStmt.setString(1, consumerKey);
                ResultSet rSet = prepStmt.executeQuery();
                while (rSet.next()) {
                    oauthApp = new OauthConsumerApp();
                    oauthApp.setId(rSet.getInt(1));
                    oauthApp.setConsumerKey(rSet.getString(2));
                    oauthApp.setConsumerSecret(rSet.getString(3));
                    oauthApp.setAppName(rSet.getString(4));
                    String appState = rSet.getString(5);
                    if (appState != null && !appState.isEmpty()) {
                        OauthState state = OauthState.valueOf(appState);
                        oauthApp.setAppState(state);
                    }
                    oauthApp.setCallbackUrl(rSet.getString(6));
                    oauthApp.setTokenExpireTime(rSet.getLong(7));
                    oauthApp.setRefreshTokenExpireTime(rSet.getLong(8));
                }
            }
            return oauthApp;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }

    public void createOrUpdate(OauthConsumerApp oauthConsumerApp) {
        OauthConsumerApp existingdo = this.getConsumerApp(oauthConsumerApp.getAppName());
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            if (existingdo != null) {
                prepStmt = connection.prepareStatement("UPDATE oauth2_consumer_apps SET consumer_secret = ?, app_name = ?, app_state = ?, callback_url = ?, token_expire_time = ?, description = ?, refresh_token_expire_time = ? WHERE consumer_key = ?");
                prepStmt.setString(1, oauthConsumerApp.getConsumerSecret());
                prepStmt.setString(2, oauthConsumerApp.getAppName());
                prepStmt.setString(3, oauthConsumerApp.getAppState().name());
                prepStmt.setString(4, oauthConsumerApp.getCallbackUrl());
                prepStmt.setLong(5, oauthConsumerApp.getTokenExpireTime());
                prepStmt.setString(6, oauthConsumerApp.getDescription());
                prepStmt.setLong(7, oauthConsumerApp.getRefreshTokenExpireTime());
                prepStmt.setString(8, existingdo.getConsumerKey());
                prepStmt.execute();
                connection.commit();
            } else {
                prepStmt = connection.prepareStatement("INSERT INTO oauth2_consumer_apps (consumer_key, consumer_secret, app_name, app_state, callback_url, token_expire_time, description, refresh_token_expire_time) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                prepStmt.setString(1, oauthConsumerApp.getConsumerKey());
                prepStmt.setString(2, oauthConsumerApp.getConsumerSecret());
                prepStmt.setString(3, oauthConsumerApp.getAppName());
                prepStmt.setString(4, oauthConsumerApp.getAppState().name());
                prepStmt.setString(5, oauthConsumerApp.getCallbackUrl());
                prepStmt.setLong(6, oauthConsumerApp.getTokenExpireTime());
                prepStmt.setString(7, oauthConsumerApp.getDescription());
                prepStmt.setLong(8, oauthConsumerApp.getRefreshTokenExpireTime());
                prepStmt.execute();
                SSODatabaseUtil.commitTransaction(connection);
                ResultSet rs = prepStmt.getGeneratedKeys();
                int last_inserted_id = 0;
                if (rs.next()) {
                    last_inserted_id = rs.getInt(1);
                }
                oauthConsumerApp.setId(last_inserted_id);
            }
            // save to cache
            MemcacheUtil.getInstance().set(oauthConsumerApp.getConsumerKey(), oauthConsumerApp);
        } catch (SQLException e) {
            e.printStackTrace();
            SSODatabaseUtil.rollbackTransaction(connection);
            log.error("Failed to store app: " + oauthConsumerApp.getAppName() + " Error while accessing the database", e);
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }

    }

    public OauthConsumerApp getConsumerApp(String appName) {
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        OauthConsumerApp consumerApp = new OauthConsumerApp();
        consumerApp.setAppName(appName);

        try {
            if (this.isConsumerAppExist(connection, consumerApp.getAppName())) {
                prepStmt = connection.prepareStatement("SELECT consumer_key, consumer_secret, app_name, app_state, callback_url, token_expire_time, description, refresh_token_expire_time, id FROM oauth2_consumer_apps WHERE app_name = ?");
                prepStmt.setString(1, consumerApp.getAppName());
                OauthConsumerApp oauthConsumerApp = this.buildOauthConsumerApp(prepStmt.executeQuery(), appName);
                return oauthConsumerApp;
            }
        } catch (SQLException e) {
            log.error("Failed to load app: " + appName, e);
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }

        return null;
    }

    public OauthConsumerApp[] getAllConsumerApp() {
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        ResultSet results = null;
        OauthConsumerApp[] rpDOs = null;
        ArrayList rpdos = new ArrayList();

        try {
            prepStmt = connection.prepareStatement("SELECT consumer_key, consumer_secret, app_name, app_state, callback_url, token_expire_time, description, refresh_token_expire_time FROM oauth2_consumer_apps ");
            results = prepStmt.executeQuery();

            while (results.next()) {
                OauthConsumerApp rpdo = new OauthConsumerApp();
                rpdo.setConsumerKey(results.getString(1));
                rpdo.setConsumerSecret(results.getString(2));
                rpdo.setAppName(results.getString(3));
                rpdo.setAppState(OauthState.valueOf(results.getString(4)));
                rpdo.setCallbackUrl(results.getString(5));
                rpdo.setTokenExpireTime(results.getLong(6));
                rpdo.setDescription(results.getString(7));
                rpdo.setRefreshTokenExpireTime(results.getLong(8));
                rpdos.add(rpdo);
            }

            rpDOs = new OauthConsumerApp[rpdos.size()];
            rpDOs = (OauthConsumerApp[]) rpdos.toArray(rpDOs);
        } catch (SQLException e) {
            log.error("Error while accessing the database to load RPs.", e);
        } finally {
            SSODatabaseUtil.closeResultSet(results);
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }

        return rpDOs;
    }

    public void update(OauthConsumerApp oauthConsumerApp) {
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;

        ResultSet results = null;

        try {
            prepStmt = connection.prepareStatement("SELECT * FROM oauth2_consumer_apps WHERE consumer_secret = ?");
            prepStmt.setString(1, oauthConsumerApp.getConsumerSecret());
            results = prepStmt.executeQuery();
            if (results != null && results.next()) {
                prepStmt = connection.prepareStatement("UPDATE oauth2_consumer_apps SET app_name = ?, app_state = ?, callback_url = ?, token_expire_time = ?, description = ?, refresh_token_expire_time = ? WHERE consumer_key = ?");
                prepStmt.setString(1, oauthConsumerApp.getAppName());
                prepStmt.setString(2, oauthConsumerApp.getAppState().name());
                prepStmt.setString(3, oauthConsumerApp.getCallbackUrl());
                prepStmt.setLong(4, oauthConsumerApp.getTokenExpireTime());
                prepStmt.setString(5, oauthConsumerApp.getDescription());
                prepStmt.setLong(6, oauthConsumerApp.getRefreshTokenExpireTime());
                prepStmt.setString(7, oauthConsumerApp.getConsumerKey());
                prepStmt.execute();
                SSODatabaseUtil.commitTransaction(connection);
                // save to cache
                MemcacheUtil.getInstance().set(oauthConsumerApp.getConsumerKey(), oauthConsumerApp);
            }
        } catch (SQLException e) {
            SSODatabaseUtil.rollbackTransaction(connection);
            log.error("Failed to update for app: " + oauthConsumerApp.getAppName() + " Error while accessing the database", e);
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }

    }

    public void delete(OauthConsumerApp oauthConsumerApp) {
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement deleteTokensStatement = null;
        PreparedStatement deleteCodesStatement = null;
        PreparedStatement prepStmt = null;

        try {
            if (this.isConsumerAppExist(connection, oauthConsumerApp.getAppName())) {
                OauthTokenDao tokenDao = new OauthTokenDao();
                OauthCodeDao codeDao = new OauthCodeDao();
                OauthToken[] activeDetailedTokens = tokenDao.getActiveAcessTokenDataByConsumerKey(oauthConsumerApp.getConsumerKey());
                String[] authorizationCodes = codeDao.getActiveAuthorizationCodesByConsumerKey(oauthConsumerApp.getConsumerKey());
                //delete tokens
                deleteTokensStatement = connection.prepareStatement("DELETE FROM oauth2_authorization_token " +
                        "WHERE consumer_id in (select id from oauth2_consumer_apps where consumer_key=?)");
                deleteTokensStatement.setString(1, oauthConsumerApp.getConsumerKey());
                deleteTokensStatement.execute();

                //delete codes
                deleteCodesStatement = connection.prepareStatement("DELETE FROM oauth2_authorization_code " +
                        "WHERE consumer_id in (select id from oauth2_consumer_apps where consumer_key=?)");
                deleteCodesStatement.setString(1, oauthConsumerApp.getConsumerKey());
                deleteCodesStatement.execute();

                // delete app
                prepStmt = connection.prepareStatement("DELETE FROM oauth2_consumer_apps WHERE app_name = ?");
                prepStmt.setString(1, oauthConsumerApp.getAppName());
                prepStmt.execute();
                SSODatabaseUtil.commitTransaction(connection);
                // delete cache
                MemcacheUtil.getInstance().delete(oauthConsumerApp.getConsumerKey());
                this.deleteCacheForDeleteConsumer(activeDetailedTokens, authorizationCodes);
            }
        } catch (SQLException e) {
            SSODatabaseUtil.rollbackTransaction(connection);
            log.error("Failed to remove app: " + oauthConsumerApp.getAppName(), e);
        } finally {
            SSODatabaseUtil.closeStatement(deleteTokensStatement);
            SSODatabaseUtil.closeStatement(deleteCodesStatement);
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }

    }

    private void deleteCacheForDeleteConsumer(OauthToken[] activeDetailedTokens, String[] authorizationCodes) {
        if (activeDetailedTokens != null && activeDetailedTokens.length > 0) {
            for (OauthToken oauthToken : activeDetailedTokens) {
                MemcacheUtil.getInstance().delete(oauthToken.getAccessToken());
            }
        }
        if (authorizationCodes != null && authorizationCodes.length > 0) {
            for (String authorizationCode : authorizationCodes) {
                MemcacheUtil.getInstance().delete(authorizationCode);
            }
        }
    }

    public void updateAppAndRevokeTokensAndAuthzCodes(String consumerKey, Properties properties,
                                                      String[] authorizationCodes, String[] accessTokens)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Updating state of client: " + consumerKey + " and revoking all access tokens and " +
                    "authorization codes.");
        }

        String action;
        if (properties.containsKey(Constants.ACTION_PROPERTY_KEY)) {
            action = properties.getProperty(Constants.ACTION_PROPERTY_KEY);
        } else {
            throw new Exception("Invalid operation.");
        }

        Connection connection = null;
        PreparedStatement updateStateStatement = null;
        PreparedStatement revokeActiveTokensStatement = null;
        PreparedStatement deactivateActiveCodesStatement = null;
        try {
            connection = SSODatabaseUtil.getDBConnection();
            if (Constants.ACTION_REVOKE.equals(action)) {
                String newAppState;
                if (properties.containsKey(Constants.OAUTH_APP_NEW_STATE)) {
                    newAppState = properties.getProperty(Constants.OAUTH_APP_NEW_STATE);
                } else {
                    throw new Exception("New App State is not specified.");
                }

                if (log.isDebugEnabled()) {
                    log.debug("Changing the state of the client: " + consumerKey + " to " + newAppState + " state.");
                }

                // update application state of the oauth app
                updateStateStatement = connection.prepareStatement("UPDATE oauth2_consumer_apps SET app_state=? " +
                        "WHERE consumer_key=?");
                updateStateStatement.setString(1, newAppState);
                updateStateStatement.setString(2, consumerKey);
                updateStateStatement.execute();
                // update cache
                OauthConsumerApp oauthConsumerApp = (OauthConsumerApp) MemcacheUtil.getInstance().get(consumerKey);
                if (oauthConsumerApp != null) {
                    oauthConsumerApp.setAppState(OauthState.valueOf(newAppState));
                    MemcacheUtil.getInstance().set(consumerKey, oauthConsumerApp);
                }
            } else if (Constants.ACTION_REGENERATE.equals(action)) {
                String newSecretKey;
                if (properties.containsKey(Constants.OAUTH_APP_NEW_SECRET_KEY)) {
                    newSecretKey = properties.getProperty(Constants.OAUTH_APP_NEW_SECRET_KEY);
                } else {
                    throw new Exception("New Consumer Secret is not specified.");
                }

                if (log.isDebugEnabled()) {
                    log.debug("Regenerating the client secret of: " + consumerKey);
                }

                // update consumer secret of the oauth app
                updateStateStatement = connection.prepareStatement("UPDATE oauth2_consumer_apps SET consumer_secret=? " +
                        "WHERE consumer_key=?");
                updateStateStatement.setString(1, newSecretKey);
                updateStateStatement.setString(2, consumerKey);
                updateStateStatement.execute();
                // update cache
                OauthConsumerApp oauthConsumerApp = (OauthConsumerApp) MemcacheUtil.getInstance().get(consumerKey);
                if (oauthConsumerApp != null) {
                    oauthConsumerApp.setConsumerSecret(newSecretKey);
                    MemcacheUtil.getInstance().set(consumerKey, oauthConsumerApp);
                }
            }

            //Revoke all active access tokens
            if (accessTokens != null && accessTokens.length != 0) {
                revokeActiveTokensStatement = connection.prepareStatement("UPDATE oauth2_authorization_token SET state=?, token=? " +
                        "WHERE consumer_id in (select id from oauth2_consumer_apps where consumer_key=?) and state=?");
                revokeActiveTokensStatement.setString(1, OauthState.INACTIVE.name());
                revokeActiveTokensStatement.setString(2, UUID.randomUUID().toString());
                revokeActiveTokensStatement.setString(3, consumerKey);
                revokeActiveTokensStatement.setString(4, OauthState.ACTIVE.name());
                revokeActiveTokensStatement.execute();
            }
            //Deactivate all active authorization codes
            deactivateActiveCodesStatement = connection.prepareStatement("UPDATE oauth2_authorization_code SET state=? " +
                    "WHERE consumer_id in (select id from oauth2_consumer_apps where consumer_key=?)");
            deactivateActiveCodesStatement.setString(1, OauthState.INACTIVE.name());
            deactivateActiveCodesStatement.setString(2, consumerKey);
            deactivateActiveCodesStatement.executeUpdate();
            // update cache
            this.updateCacheForChangeConsumer(authorizationCodes, accessTokens);

            SSODatabaseUtil.commitTransaction(connection);

        } catch (SQLException e) {
            SSODatabaseUtil.rollbackTransaction(connection);
            log.error("Error while executing the SQL statement.", e);
        } finally {
            SSODatabaseUtil.closeStatement(updateStateStatement);
            SSODatabaseUtil.closeStatement(revokeActiveTokensStatement);
            SSODatabaseUtil.closeAllConnections(connection, null, deactivateActiveCodesStatement);
        }
    }

    private void updateCacheForChangeConsumer(String[] authorizationCodes, String[] accessTokens) {
        // save access token
        if (accessTokens != null && accessTokens.length != 0) {
            for (String accessToken : accessTokens) {
                OauthToken oauthToken = (OauthToken) MemcacheUtil.getInstance().get(accessToken);
                if (oauthToken != null) {
                    oauthToken.setState(OauthState.INACTIVE);
                    MemcacheUtil.getInstance().set(accessToken, oauthToken);
                }
            }
        }
        // save authorization code
        if (authorizationCodes != null && authorizationCodes.length != 0) {
            for (String authorizationCode : authorizationCodes) {
                OauthCode oauthCode = (OauthCode) MemcacheUtil.getInstance().get(authorizationCode);
                if (oauthCode != null) {
                    oauthCode.setState(OauthState.INACTIVE);
                    MemcacheUtil.getInstance().set(authorizationCode, oauthCode);
                }
            }
        }
    }

    public String getOauthApplicationState(String consumerkey) {
        OauthConsumerApp consumerApp = this.getByConsumerKey(consumerkey);
        if (consumerApp != null)
            return consumerApp.getAppState().name();
        else
            return null;
    }

    private boolean isConsumerAppExist(Connection connection, String consumerAppName) throws SQLException {
        PreparedStatement prepStmt = null;
        ResultSet results = null;
        boolean result = false;

        try {
            prepStmt = connection.prepareStatement("SELECT * FROM oauth2_consumer_apps WHERE app_name = ?");
            prepStmt.setString(1, consumerAppName);
            results = prepStmt.executeQuery();
            if (results != null && results.next()) {
                result = true;
            }
        } finally {
            SSODatabaseUtil.closeResultSet(results);
            SSODatabaseUtil.closeStatement(prepStmt);
        }

        return result;
    }

    private OauthConsumerApp buildOauthConsumerApp(ResultSet results, String appName) {
        OauthConsumerApp consumerApp = new OauthConsumerApp();
        try {
            if (!results.next()) {
                if (log.isDebugEnabled()) {
                    log.debug("RememberMe token not found for the app " + appName);
                }
                return null;
            }
            consumerApp.setConsumerKey(results.getString(1));
            consumerApp.setConsumerSecret(results.getString(2));
            consumerApp.setAppName(results.getString(3));
            consumerApp.setAppState(OauthState.valueOf(results.getString(4)));
            consumerApp.setCallbackUrl(results.getString(5));
            consumerApp.setTokenExpireTime(results.getLong(6));
            consumerApp.setDescription(results.getString(7));
            consumerApp.setRefreshTokenExpireTime(results.getLong(8));
            consumerApp.setId(results.getInt(9));
        } catch (SQLException e) {
            log.error("Error while accessing the database", e);
        }
        return consumerApp;
    }
}


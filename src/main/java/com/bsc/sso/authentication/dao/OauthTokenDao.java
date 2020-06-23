package com.bsc.sso.authentication.dao;

import com.bsc.sso.authentication.model.OauthCode;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;
import com.bsc.sso.authentication.model.OauthToken;
import com.bsc.sso.authentication.util.MemcacheUtil;
import com.bsc.sso.authentication.util.SSODatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class OauthTokenDao {
    private OauthConsumerAppDao oauthConsumerAppDao = new OauthConsumerAppDao();

    public void addToken(OauthToken oauthToken) {
        OauthConsumerApp oauthConsumerApp = oauthConsumerAppDao.getByConsumerKey(oauthToken.getClientId());
        int consumerId = oauthConsumerApp.getId();
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            String sql = "insert into oauth2_authorization_token (token, consumer_id, username, state, time_created, token_expire_time, refresh_token_expire_time, access_token, refresh_token) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            prepStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, oauthToken.getToken());
            prepStmt.setInt(2, consumerId);
            prepStmt.setString(3, oauthToken.getUserName());
            prepStmt.setString(4, oauthToken.getState().name());
            prepStmt.setTimestamp(5, new Timestamp(oauthToken.getTimeCreated().getTime()));
            prepStmt.setLong(6, oauthToken.getTokenExpireTime());
            prepStmt.setLong(7, oauthToken.getRefreshTokenExpireTime());
            prepStmt.setString(8, oauthToken.getAccessToken());
            prepStmt.setString(9, oauthToken.getRefreshToken());
            prepStmt.executeUpdate();
//            ResultSet rs = prepStmt.getGeneratedKeys();
//            if(rs.next()) {
//                int last_inserted_id = rs.getInt(1);
//                return last_inserted_id;
//            }
            // commit transaction
            SSODatabaseUtil.commitTransaction(connection);
            // save to cache
            MemcacheUtil.getInstance().set(oauthToken.getAccessToken(), oauthToken);
        } catch (SQLException var10) {
            SSODatabaseUtil.rollbackTransaction(connection);
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }

    public OauthToken[] getActiveAcessTokenDataByConsumerKey(String consumerKey) {
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        ResultSet results = null;
        OauthToken[] rpDOs = null;
        ArrayList rpdos = new ArrayList();

        try {
            prepStmt = connection.prepareStatement("SELECT token, consumer_id, username, state, time_created, token_expire_time, refresh_token_expire_time, access_token, refresh_token " +
                    "FROM oauth2_authorization_token " +
                    "WHERE consumer_id in (select id from oauth2_consumer_apps where consumer_key=?) and state=?");
            prepStmt.setString(1, consumerKey);
            prepStmt.setString(2, OauthState.ACTIVE.name());
            results = prepStmt.executeQuery();

            while (results.next()) {
                OauthToken rpdo = new OauthToken();
                rpdo.setToken(results.getString(1));
                rpdo.setConsumerId(results.getInt(2));
                rpdo.setUserName(results.getString(3));
                rpdo.setState(OauthState.valueOf(results.getString(4)));
                rpdo.setTimeCreated((results.getDate(5)));
                rpdo.setTokenExpireTime(results.getLong(6));
                rpdo.setRefreshTokenExpireTime(results.getLong(7));
                rpdo.setAccessToken(results.getString(8));
                rpdo.setRefreshToken(results.getString(9));
                rpdos.add(rpdo);
            }

            rpDOs = new OauthToken[rpdos.size()];
            rpDOs = (OauthToken[]) rpdos.toArray(rpDOs);
        } catch (SQLException var10) {
        } finally {
            SSODatabaseUtil.closeResultSet(results);
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }

        return rpDOs;
    }

    public OauthToken getTokenByRefreshToken(String refreshToken) {
        OauthToken oauthToken = null;
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            prepStmt = connection.prepareStatement("select id, token, consumer_id, username, state, time_created, token_expire_time, refresh_token_expire_time, access_token, refresh_token" +
                    " from oauth2_authorization_token where refresh_token=?");
            prepStmt.setString(1, refreshToken);
            ResultSet rSet = prepStmt.executeQuery();
            while (rSet.next()) {
                oauthToken = new OauthToken();
                oauthToken.setId(rSet.getInt(1));
                oauthToken.setToken(rSet.getString(2));
                oauthToken.setConsumerId(rSet.getInt(3));
                oauthToken.setUserName(rSet.getString(4));
                String appState = rSet.getString(5);
                if (appState != null && !appState.isEmpty()) {
                    OauthState state = OauthState.valueOf(appState);
                    oauthToken.setState(state);
                }
                Timestamp timestamp = rSet.getTimestamp(6);
                if (timestamp != null) {
                    Date date = new Date(timestamp.getTime());
                    oauthToken.setTimeCreated(date);
                }
                oauthToken.setTokenExpireTime(rSet.getLong(7));
                oauthToken.setRefreshTokenExpireTime(rSet.getLong(8));
                oauthToken.setAccessToken(rSet.getString(9));
                oauthToken.setRefreshToken(rSet.getString(10));
                // only select first result
                break;
            }
            return oauthToken;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }

    public OauthToken getTokenByAccessToken(String accessToken) {
        OauthToken oauthToken = null;
        // get from cached
        oauthToken = (OauthToken) MemcacheUtil.getInstance().get(accessToken);
        if (oauthToken != null) return oauthToken;
        // get from database
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            prepStmt = connection.prepareStatement("select id, token, consumer_id, username, state, time_created, token_expire_time, refresh_token_expire_time, access_token, refresh_token" +
                    " from oauth2_authorization_token where access_token=?");
            prepStmt.setString(1, accessToken);
            ResultSet rSet = prepStmt.executeQuery();
            while (rSet.next()) {
                oauthToken = new OauthToken();
                oauthToken.setId(rSet.getInt(1));
                oauthToken.setToken(rSet.getString(2));
                oauthToken.setConsumerId(rSet.getInt(3));
                oauthToken.setUserName(rSet.getString(4));
                String appState = rSet.getString(5);
                if (appState != null && !appState.isEmpty()) {
                    OauthState state = OauthState.valueOf(appState);
                    oauthToken.setState(state);
                }
                Timestamp timestamp = rSet.getTimestamp(6);
                if (timestamp != null) {
                    Date date = new Date(timestamp.getTime());
                    oauthToken.setTimeCreated(date);
                }
                oauthToken.setTokenExpireTime(rSet.getLong(7));
                oauthToken.setRefreshTokenExpireTime(rSet.getLong(8));
                oauthToken.setAccessToken(rSet.getString(9));
                oauthToken.setRefreshToken(rSet.getString(10));
                // only select first result
                break;
            }
            return oauthToken;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }

    public void deleteToken(OauthToken oauthToken) {
        String accessToken = oauthToken.getAccessToken();
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            prepStmt = connection.prepareStatement("DELETE FROM oauth2_authorization_token " +
                    "where access_token=?");
            prepStmt.setString(1, accessToken);
            prepStmt.executeUpdate();
            SSODatabaseUtil.commitTransaction(connection);
            // update cache
            MemcacheUtil.getInstance().delete(accessToken);
        } catch (SQLException e) {
            SSODatabaseUtil.rollbackTransaction(connection);
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }
}

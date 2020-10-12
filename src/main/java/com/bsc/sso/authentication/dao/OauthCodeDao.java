package com.bsc.sso.authentication.dao;

import com.bsc.sso.authentication.model.OauthCode;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;
import com.bsc.sso.authentication.util.MemcacheUtil;
import com.bsc.sso.authentication.util.SSODatabaseUtil;
import com.bsc.sso.authentication.util.StringPool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class OauthCodeDao {

    private final OauthConsumerAppDao oauthConsumerAppDao = new OauthConsumerAppDao();

    public final static int TIME_EXPIRED = 100000000;

    public void addAuthCode(String code, String consumerKey, String userName) {
        OauthConsumerApp oauthConsumerApp = oauthConsumerAppDao.getByConsumerKey(consumerKey);
        int consumerId = oauthConsumerApp.getId();
        OauthCode oauthCode = new OauthCode();
        oauthCode.setCode(code);
        oauthCode.setConsumerId(consumerId);
        oauthCode.setUserName(userName);
        oauthCode.setState(OauthState.ACTIVE);
        oauthCode.setTimeCreated(new Date());
        oauthCode.setValidityPeriod(TIME_EXPIRED);

        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            prepStmt = connection.prepareStatement(StringPool.ADD_AUTH_CODE_QUERY);
            prepStmt.setString(1, oauthCode.getCode());
            prepStmt.setInt(2, oauthCode.getConsumerId());
            prepStmt.setString(3, oauthCode.getUserName());
            prepStmt.setString(4, oauthCode.getState().toString());
            prepStmt.setTimestamp(5, new Timestamp(oauthCode.getTimeCreated().getTime()));
            prepStmt.setLong(6, oauthCode.getValidityPeriod());
            prepStmt.execute();
            SSODatabaseUtil.commitTransaction(connection);
            // save to cache
            MemcacheUtil.getInstance().set(code, oauthCode);
        } catch (SQLException e) {
            SSODatabaseUtil.rollbackTransaction(connection);
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }

    public void deleteOauthCode(String code) {
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            prepStmt = connection.prepareStatement("DELETE FROM oauth2_authorization_code " +
                    "where code=?");
            prepStmt.setString(1, code);
            prepStmt.executeUpdate();
            SSODatabaseUtil.commitTransaction(connection);
            // update cache
            MemcacheUtil.getInstance().delete(code);
        } catch (SQLException e) {
            SSODatabaseUtil.rollbackTransaction(connection);
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }

    public OauthCode getByCode(String authCode) {
        OauthCode oauthCode = null;
        // get from cached
        oauthCode = (OauthCode) MemcacheUtil.getInstance().get(authCode);
        if (oauthCode != null) return oauthCode;
        // get from database
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            prepStmt = connection.prepareStatement(StringPool.GET_BY_CODE);
            prepStmt.setString(1, authCode);
            ResultSet rSet = prepStmt.executeQuery();
            while (rSet.next()) {
                oauthCode = new OauthCode();
                oauthCode.setId(rSet.getInt(1));
                oauthCode.setCode(rSet.getString(2));
                oauthCode.setConsumerId(rSet.getInt(3));
                oauthCode.setUserName(rSet.getString(4));
                String appState = rSet.getString(5);
                if (appState != null && !appState.isEmpty()) {
                    OauthState state = OauthState.valueOf(appState);
                    oauthCode.setState(state);
                }
                Timestamp timestamp = rSet.getTimestamp(6);
                if (timestamp != null) {
                    Date date = new Date(timestamp.getTime());
                    oauthCode.setTimeCreated(date);
                }
                oauthCode.setValidityPeriod(rSet.getLong(7));
                // only select first result
                break;
            }
            return oauthCode;
        } catch (SQLException e) {
            LOGGER.error("Error when get code " + e);
            return null;
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }

    public String[] getActiveAuthorizationCodesByConsumerKey(String consumerKey) {
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        ResultSet results = null;
        String[] rpDOs = null;
        ArrayList rpdos = new ArrayList();

        try {
            prepStmt = connection.prepareStatement("SELECT code FROM oauth2_authorization_code" +
                    "WHERE consumer_id in (select id from oauth2_consumer_apps where consumer_key=?) and state=?");
            prepStmt.setString(1, consumerKey);
            prepStmt.setString(2, OauthState.ACTIVE.name());
            results = prepStmt.executeQuery();
            while (results.next()) {
                rpdos.add((results.getString(1)));
            }
            rpDOs = new String[rpdos.size()];
            rpDOs = (String[]) rpdos.toArray(rpDOs);
        } catch (SQLException var10) {
        } finally {
            SSODatabaseUtil.closeResultSet(results);
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }

        return rpDOs;
    }

    private final static Logger LOGGER = Logger.getLogger(OauthCodeDao.class);
}

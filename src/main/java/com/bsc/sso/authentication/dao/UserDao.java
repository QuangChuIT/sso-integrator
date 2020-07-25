package com.bsc.sso.authentication.dao;

import com.bsc.sso.authentication.model.User;
import com.bsc.sso.authentication.util.SSODatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public User getByUsername(String username) {
        User user = null;
        // get from database
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            prepStmt = connection.prepareStatement("select id, username, password from user where username=?");
            prepStmt.setString(1, username);
            ResultSet rSet = prepStmt.executeQuery();
            while (rSet.next()) {
                user = new User();
                user.setId(rSet.getInt(1));
                user.setUsername(rSet.getString(2));
                user.setPassword(rSet.getString(3));
                // only select first result
                break;
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }

    public void changePassword(String username, String password) {
        Connection connection = SSODatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            prepStmt = connection.prepareStatement("update user set password=? where username=?");
            prepStmt.setString(1, password);
            prepStmt.setString(2, username);
            prepStmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            SSODatabaseUtil.rollbackTransaction(connection);
        } finally {
            SSODatabaseUtil.closeStatement(prepStmt);
            SSODatabaseUtil.closeConnection(connection);
        }
    }
}

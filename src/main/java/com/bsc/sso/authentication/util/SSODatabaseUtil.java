package com.bsc.sso.authentication.util;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class SSODatabaseUtil {
    private static final Logger log = Logger.getLogger(SSODatabaseUtil.class);

    public SSODatabaseUtil() {
    }

    public static Connection getDBConnection() {
        try {
            return ConfigUtil.getInstance().getConnectionString();
        } catch (Exception e) {
            log.error("Database error. Could not get DBConnection. - " + e.getMessage(), e);
        }
        return null;
    }

    public static void closeAllConnections(Connection dbConnection, ResultSet rs, PreparedStatement prepStmt) {
        closeResultSet(rs);
        closeStatement(prepStmt);
        closeConnection(dbConnection);
    }

    public static void closeConnection(Connection dbConnection) {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException var2) {
                log.error("Database error. Could not close statement. Continuing with others. - " + var2.getMessage(), var2);
            }
        }

    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException var2) {
                log.error("Database error. Could not close result set  - " + var2.getMessage(), var2);
            }
        }

    }

    public static void closeStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException var2) {
                log.error("Database error. Could not close statement. Continuing with others. - " + var2.getMessage(), var2);
            }
        }

    }

    public static void rollBack(Connection dbConnection) {
        rollbackTransaction(dbConnection);
    }

    public static void rollbackTransaction(Connection dbConnection) {
        try {
            if (dbConnection != null) {
                dbConnection.rollback();
            }
        } catch (SQLException var3) {
            log.error("An error occurred while rolling back transactions. ", var3);
        }
    }

    public static void commitTransaction(Connection dbConnection) {
        try {
            if (dbConnection != null) {
                dbConnection.commit();
            }
        } catch (SQLException var3) {
            log.error("An error occurred while commit transactions. ", var3);
        }
    }
}


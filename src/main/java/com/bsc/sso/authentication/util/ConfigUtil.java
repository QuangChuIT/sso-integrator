package com.bsc.sso.authentication.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConfigUtil {
    private static volatile ConfigUtil instance;
    private static Log log = LogFactory.getLog(ConfigUtil.class);
    private Properties prop;
    private static final BasicDataSource dataSource = new BasicDataSource();

    private ConfigUtil() {
        this.initDatabaseConfig();
        this.initPropertiesConfig();
    }

    public static ConfigUtil getInstance() {
        if (instance == null) {
            synchronized (ConfigUtil.class) {
                if (instance == null) {
                    instance = new ConfigUtil();
                }
            }
        }
        return instance;
    }

    public Connection getConnectionString() throws Exception {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    private void initDatabaseConfig() {
        try {
            ClassLoader classLoader = new FileUtil().getClass().getClassLoader();
            String urlEncoded = classLoader.getResource("database-config.xml").getFile();
            String urlDecoded = URLDecoder.decode(urlEncoded, "UTF-8");
            File file = new File(urlDecoded);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            String dbName = document.getElementsByTagName("DBName").item(0).getTextContent();
            String userName = document.getElementsByTagName("UserName").item(0).getTextContent();
            String password = document.getElementsByTagName("Password").item(0).getTextContent();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl(dbName);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
        } catch (Exception e) {
            log.error("Error when looking up the Identity Data Source.", e);
        }
    }

    private void initPropertiesConfig() {
        try {
            prop = new Properties();
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return prop.getProperty(key);
    }
}

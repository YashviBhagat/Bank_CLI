package com.bankcli.repository;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
    private static final ConnectionFactory connectionFactory = new ConnectionFactory();
    private Properties props = new Properties();

    private ConnectionFactory() {
        try {
            props.load(new FileReader("src/main/resources/db.properties"));
        } catch (IOException e) {
            logger.error("Could not load db.properties: {}", e.getMessage());
        }
    }

    public static ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    props.getProperty("DB_URL"),
                    props.getProperty("DB_USER"),
                    props.getProperty("DB_PASSWORD"));
        } catch (SQLException e) {
            logger.error("Database connection failed: {}", e.getMessage());
            throw new IllegalStateException("Could not connect to the database", e);
        }
    }
}

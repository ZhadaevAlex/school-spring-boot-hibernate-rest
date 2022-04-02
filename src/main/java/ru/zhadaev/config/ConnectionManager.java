package ru.zhadaev.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
    private final String url;
    private final String user;
    private final String password;
    private Connection connection = null;

    public ConnectionManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            connection = DriverManager.getConnection(url, user, password);
            log.debug("New DB connection created");
            return connection;
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new IllegalStateException(e);
        }
    }
}

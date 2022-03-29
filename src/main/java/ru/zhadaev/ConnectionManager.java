package ru.zhadaev;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConnectionManager {
    private final Logger log;
    String url;
    String user;
    String password;

    public ConnectionManager() {
        url = PropertiesReader.getInstance().getProperty("URL");
        user = PropertiesReader.getInstance().getProperty("USER");
        password = PropertiesReader.getInstance().getProperty("PASSWORD");
        log = Logger.getLogger(this.getClass().getName());
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch(SQLException e) {
            log.severe("Connection creation error");
            loggingException(e);
        }

        return connection;
    }

    private static class Lazy {
        private static final ConnectionManager instance = new ConnectionManager();
    }

    public static ConnectionManager getInstance() {
        return Lazy.instance;
    }

    public void loggingException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String str = sw.toString();
        log.severe(str);
    }
}

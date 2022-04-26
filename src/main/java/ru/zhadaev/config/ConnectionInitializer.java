package ru.zhadaev.config;

public class ConnectionInitializer {
    private static final String FILE_PROP_NAME = "application.properties";
    private static final String URL_PROP_NAME = "URL";
    private static final String USER_PROP_NAME = "USER";
    private static final String PASSWORD_PROP_NAME = "PASSWORD";

    private String url;
    private String user;
    private String password;

    public ConnectionInitializer() {
        PropertiesReader propertiesReader = new PropertiesReader(FILE_PROP_NAME);
        this.url = propertiesReader.getProperty(URL_PROP_NAME);
        this.user = propertiesReader.getProperty(USER_PROP_NAME);
        this.password = propertiesReader.getProperty(PASSWORD_PROP_NAME);
    }

    public ConnectionManager createConnection() {
        return new ConnectionManager(url, user, password);
    }
}

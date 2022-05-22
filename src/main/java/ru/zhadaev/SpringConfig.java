package ru.zhadaev;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.config.PropertiesReader;

@Configuration
@ComponentScan("ru.zhadaev")
public class SpringConfig {
    private static final String FILE_PROP_NAME = "application.properties";
    private static final String URL_PROP_NAME = "URL";
    private static final String USER_PROP_NAME = "USER";
    private static final String PASSWORD_PROP_NAME = "PASSWORD";

    @Bean
    PropertiesReader propertiesReader() {
        return new PropertiesReader(FILE_PROP_NAME);
    }
    
    @Bean
    ConnectionManager connectionManager(PropertiesReader propertiesReader) {
        String url = propertiesReader.getProperty(URL_PROP_NAME);
        String user = propertiesReader.getProperty(USER_PROP_NAME);
        String password = propertiesReader.getProperty(PASSWORD_PROP_NAME);

        return new ConnectionManager(url, user, password);
    }
}

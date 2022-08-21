package ru.zhadaev.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hibernate.connection")
@Data
public class HibernateProperties {
    private String driverClass;
    private String url;
    private String username;
    private String password;
}

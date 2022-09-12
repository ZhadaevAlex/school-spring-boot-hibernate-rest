package ru.zhadaev.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.jpa.properties.hibernate.connection")
public class HibernateProperties {
    private final JpaProperties properties;

    private String url;
    private String username;
    private String password;
    private String driverClass;

    public Properties getHibernateProperties(){
        Properties props = new Properties();
        properties.getProperties().forEach(props::setProperty);
        return props;
    }
}

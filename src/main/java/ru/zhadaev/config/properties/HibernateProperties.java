package ru.zhadaev.config.properties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "spring.jpa.properties.hibernate.connection")
@Data
@RequiredArgsConstructor
public class HibernateProperties {
    private final JpaProperties properties;

    private String driverClass;
    private String url;
    private String username;
    private String password;

    public Properties getHibernateProperties(){
        Properties props = new Properties();
        properties.getProperties().forEach(props::setProperty);
        return props;
    }
}

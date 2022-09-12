package ru.zhadaev.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

public class DataSourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String showSql;
}

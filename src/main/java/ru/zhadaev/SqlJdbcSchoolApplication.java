package ru.zhadaev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("ru.zhadaev")
public class SqlJdbcSchoolApplication {
    public static void main(String[] args) {
        SpringApplication.run(SqlJdbcSchoolApplication.class, args);
    }
}

package ru.zhadaev.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.zhadaev.config.FileReader;
import ru.zhadaev.config.PropertiesReader;

import java.io.File;
import java.util.stream.Collectors;

@Component
public class TablesCreator {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TablesCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTables() {
        PropertiesReader propertiesReader = new PropertiesReader("application.properties");
        String sqlFile = propertiesReader.getProperty("SQL_FILE");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(sqlFile).getFile());
        String absolutePath = file.getAbsolutePath();

        String sqlQuery = new FileReader().read(absolutePath).stream().collect(Collectors.joining());
        jdbcTemplate.execute(sqlQuery);
    }
}

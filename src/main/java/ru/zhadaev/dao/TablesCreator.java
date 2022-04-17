package ru.zhadaev.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.config.FileReader;
import ru.zhadaev.config.PropertiesReader;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.exception.IsNotFileException;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.sql.*;
import java.util.stream.Collectors;

public class TablesCreator {
    private static final Logger logger = LoggerFactory.getLogger(TablesCreator.class);
    private final ConnectionManager connectionManager;

    public TablesCreator(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void createTables() throws DAOException, NoSuchFileException, IsNotFileException {
        Connection connection = connectionManager.getConnection();

        PropertiesReader propertiesReader = new PropertiesReader("application.properties");
        String sqlFile = propertiesReader.getProperty("SQL_FILE");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(sqlFile).getFile());
        String absolutePath = file.getAbsolutePath();

        String sqlQuery = new FileReader().read(absolutePath).stream().collect(Collectors.joining());

        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be create tables", e);
        }
    }
}

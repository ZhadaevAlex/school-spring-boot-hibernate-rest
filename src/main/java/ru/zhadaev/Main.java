package ru.zhadaev;

import java.nio.file.NoSuchFileException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException, NoSuchFileException, IsNotFileException {
        String url = PropertiesReader.getInstance().getProperty("URL");
        String user = PropertiesReader.getInstance().getProperty("USER");
        String password = PropertiesReader.getInstance().getProperty("PASSWORD");
        String sqlFile = PropertiesReader.getInstance().getProperty("SQL_FILE");

        String sqlQuery = new SqlFileReader().read(sqlFile);

        Connection con = DriverManager.getConnection(url, user, password);
        Statement statement = con.createStatement();
        statement.executeLargeUpdate(sqlQuery);
    }
}

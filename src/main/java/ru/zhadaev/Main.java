package ru.zhadaev;

import java.nio.file.NoSuchFileException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException, NoSuchFileException, IsNotFileException {
        Map<String, String> subjects = new HashMap<>();
        subjects.put("Literature", "Subject Literature");
        subjects.put("Astronomy", "Subject Astronomy");
        subjects.put("Biology", "Subject Biology");
        subjects.put("Music", "Subject Music");
        subjects.put("Botany", "Subject Botany");
        subjects.put("Chemistry", "Subject Chemistry");
        subjects.put("Computer science", "Subject Computer science");
        subjects.put("Economics", "Subject Economics");
        subjects.put("Math", "Subject Math");
        subjects.put("History", "Subject History");

        List<String> firstNames = new ArrayList<>();
        Collections.addAll(firstNames,
                "Oliver", "Jack", "Harry", "Jacob", "Charlie",
                "Thomas", "Oscar", "William", "James", "George",
                "Amelia", "Olivia", "Emily", "Ava", "Jessica",
                "Isabella", "Sophie", "Mia", "Ruby", "Lily");

        List<String> lastNames = new ArrayList<>();
        Collections.addAll(lastNames,
                "Anderson", "Thomas", "Jackson", "White", "Harris",
                "Martin", "Thompson", "Wood", "Lewis", "Scott",
                "Cooper", "King", "Green",  "Walker", "Edwards",
                "Turner", "Morgan", "Baker", "Hill", "Phillips");

        School school = new SchoolCreator(10, subjects, 200,
                firstNames, lastNames, 10, 30).createSchool();

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

package ru.zhadaev;

import ru.zhadaev.config.PropertiesReader;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.School;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.impl.GroupRepository;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.util.creation.SchoolCreator;
import ru.zhadaev.util.creation.StudentsCreator;

import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws SQLException {

        PropertiesReader propertiesReader = new PropertiesReader("application.properties");
        String url = propertiesReader.getProperty("URL");
        String user = propertiesReader.getProperty("USER");
        String password = propertiesReader.getProperty("PASSWORD");
        ConnectionManager connectionManager = new ConnectionManager(url, user, password);
        GroupRepository groupRepository = new GroupRepository(connectionManager);


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
                "Cooper", "King", "Green", "Walker", "Edwards",
                "Turner", "Morgan", "Baker", "Hill", "Phillips");

        List<Student> students = new StudentsCreator().createStudents(200, firstNames, lastNames);
        School school = new SchoolCreator(10, 10, 30,
                subjects, 1, 3).createSchool(students);

        Group groupIn1 = new Group("RR-16");
//        Group groupOut1 = groupRepository.save(groupIn1);
//
//        Group groupIn2 = new Group("RR-17");
//        Group groupOut2 = groupRepository.save(groupIn2);

        Optional<Group> pg = groupRepository.findById(44);
        if (pg.isPresent()) {
            System.out.println(pg.get().getId());
            System.out.println(pg.get().getName());
        } else {
            System.out.println("Null");
        }

        System.out.println(groupRepository.existsById(66));

        List<Group> groups = groupRepository.findAll();

        long a = groupRepository.count();
        System.out.println(a);

        groupRepository.deleteById(3);

        int b = 2;
    }
}

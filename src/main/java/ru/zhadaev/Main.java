package ru.zhadaev;

import jdk.nashorn.internal.runtime.options.Option;
import ru.zhadaev.config.PropertiesReader;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.School;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.impl.CourseRepository;
import ru.zhadaev.dao.repository.impl.GroupRepository;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.repository.impl.StudentRepository;
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

//        Group groupIn1 = new Group("RR-16");
//        Optional<Group> pg = groupRepository.findById(44);
//        if (pg.isPresent()) {
//            System.out.println(pg.get().getId());
//            System.out.println(pg.get().getName());
//        } else {
//            System.out.println("Null");
//        }
//
//        System.out.println(groupRepository.existsById(66));
//
//        List<Group> groups = groupRepository.findAll();
//
//        long a = groupRepository.count();
//        System.out.println(a);
//
//        groupRepository.deleteById(3);
//
//        int b = 2;

        Course courseIn1 = new Course("Math");
        courseIn1.setId(1);
        courseIn1.setDescription("Subject Math");
        CourseRepository cr = new CourseRepository(connectionManager);
        Course courseOut1 = cr.save(courseIn1);

        Optional<Course> courseOut2 = cr.findById(1);
        Optional<Course> courseOut3 = cr.findById(33);
        List<Course> courses = cr.findAll();
        long count = cr.count();
        cr.deleteById(3);
        cr.delete(courseIn1);
        cr.deleteAll();

        StudentRepository sr = new StudentRepository(connectionManager);
        sr.save(new Student("aaa", "bbb"));

        int c = 3;




    }
}

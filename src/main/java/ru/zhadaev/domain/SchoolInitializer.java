package ru.zhadaev.domain;

import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.config.PropertiesReader;
import ru.zhadaev.dao.TablesCreator;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.School;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.dao.repository.impl.CourseRepository;
import ru.zhadaev.dao.repository.impl.GroupRepository;
import ru.zhadaev.dao.repository.impl.StudentRepository;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.exception.IsNotFileException;
import ru.zhadaev.util.creation.SchoolCreator;
import ru.zhadaev.util.creation.StudentsCreator;

import java.nio.file.NoSuchFileException;
import java.util.*;

public class SchoolInitializer {
    ConnectionManager connectionManager;

    public SchoolInitializer() {
        PropertiesReader propertiesReader = new PropertiesReader("application.properties");
        String url = propertiesReader.getProperty("URL");
        String user = propertiesReader.getProperty("USER");
        String password = propertiesReader.getProperty("PASSWORD");
        this.connectionManager = new ConnectionManager(url, user, password);
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void initialize() throws DAOException, NoSuchFileException, IsNotFileException {
        SchoolInitData schoolInitData = new SchoolInitData();

        List<Student> students = new StudentsCreator().createStudents(
                SchoolInitData.NUMBER_STUDENTS,
                schoolInitData.getFirstNames(),
                schoolInitData.getLastNames());

        School school = new SchoolCreator(
                SchoolInitData.NUMBER_GROUPS,
                SchoolInitData.MIN_STUDENTS_IN_GROUP,
                SchoolInitData.MAX_STUDENTS_IN_GROUP,
                schoolInitData.getSubjects(),
                SchoolInitData.MIN_COURSES,
                SchoolInitData.MAX_COURSES).createSchool(students);

        createTables();
        initializeGroup(school);
        initializeCourses(school);
        initializeStudents(school);
    }

    private void createTables() throws DAOException, NoSuchFileException, IsNotFileException {
        TablesCreator tablesCreator = new TablesCreator(connectionManager);
        tablesCreator.createTables();
    }

    void initializeGroup(School school) throws DAOException {
        CrudRepository<Group, Integer> groupRepository = new GroupRepository(connectionManager);
        groupRepository.deleteAll();
        List<Group> groups = new ArrayList<>();
        for (Group group : school.getGroups()) {
            groups.add(groupRepository.save(group));
        }

        school.setGroups(groups);
    }

    void initializeCourses(School school) throws DAOException {
        CrudRepository<Course, Integer> courseRepository = new CourseRepository(connectionManager);
        courseRepository.deleteAll();
        List<Course> courses = new ArrayList<>();
        for (Course course : school.getCourses()) {
            courses.add(courseRepository.save(course));
        }

        school.setCourses(courses);
    }

    void initializeStudents(School school) throws DAOException {
        for (Student student : school.getStudents()) {
            Group groupWithId = null;
            if (student.getGroup() != null) {
                groupWithId = school.getGroups().stream().filter(p -> p.getName().equals(student.getGroup().getName())).findFirst().orElse(student.getGroup());
            }

            student.setGroup(groupWithId);

            List<Course> coursesWithId = new ArrayList<>();
            for (Course course : student.getCourses()) {
                coursesWithId.add(school.getCourses().stream().filter(p -> p.getName().equals(course.getName())).findFirst().orElse(course));
            }

            student.setCourses(coursesWithId);
        }

        CrudRepository<Student, Integer> studentRepository = new StudentRepository(connectionManager);
        List<Student> studentsWithId = new ArrayList<>();
        for (Student student : school.getStudents()) {
            studentsWithId.add(studentRepository.save(student));
        }
    }
}

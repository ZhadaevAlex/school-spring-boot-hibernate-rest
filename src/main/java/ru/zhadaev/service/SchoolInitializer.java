package ru.zhadaev.service;

import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.TablesCreator;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.School;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.impl.CourseRepository;
import ru.zhadaev.dao.repository.impl.GroupRepository;
import ru.zhadaev.dao.repository.impl.StudentRepository;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.exception.IsNotFileException;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

public class SchoolInitializer {
    ConnectionManager connectionManager;

    public SchoolInitializer(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void initialize(School school) throws DAOException, NoSuchFileException, IsNotFileException {
        createTables();

        List<Group> groupsDb = initializeGroup(school.getGroups());
        List<Course> coursesDb = initializeCourses(school.getCourses());

        for (Student student : school.getStudents()) {
            if (student.getGroup() != null) {
                Integer id = groupsDb.stream()
                        .filter(p -> p.getName()
                                .equals(student.getGroup().getName()))
                        .findFirst().get().getId();
                student.getGroup().setId(id);
            }

            for (Course courseStudent : student.getCourses()) {
                Integer id = coursesDb.stream()
                        .filter(p -> p.getName().equals(courseStudent.getName())
                                && p.getDescription().equals(courseStudent.getDescription()))
                        .findFirst().get().getId();

                courseStudent.setId(id);
            }
        }

        List<Student> studentsDb = initializeStudents(school.getStudents());

        signStudentsOnCourses(studentsDb);
    }

    private void createTables() throws DAOException, NoSuchFileException, IsNotFileException {
        TablesCreator tablesCreator = new TablesCreator(connectionManager);
        tablesCreator.createTables();
    }

    List<Group> initializeGroup(List<Group> groups) throws DAOException {
        GroupRepository groupRepository = new GroupRepository(connectionManager);
        List<Group> groupsDb = new ArrayList<>();
        for (Group group : groups) {
            groupsDb.add(groupRepository.save(group));
        }

        return groupsDb;
    }

    List<Course> initializeCourses(List<Course> courses) throws DAOException {
        CourseRepository courseRepository = new CourseRepository(connectionManager);
        List<Course> coursesDb = new ArrayList<>();
        for (Course course : courses) {
            coursesDb.add(courseRepository.save(course));
        }

        return coursesDb;
    }

    List<Student> initializeStudents(List<Student> students) throws DAOException {
        List<Student> studentsDb = new ArrayList<>();
        StudentRepository studentRepository = new StudentRepository(connectionManager);
        for (Student student : students) {
            studentsDb.add(studentRepository.save(student));
        }

        return studentsDb;
    }

    void signStudentsOnCourses(List<Student> students) throws DAOException {
        StudentRepository studentRepository = new StudentRepository(connectionManager);
        for (Student student : students) {
            studentRepository.signOnCourses(student, student.getCourses());
        }
    }
}

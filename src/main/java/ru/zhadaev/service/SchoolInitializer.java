package ru.zhadaev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.TablesCreator;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.School;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.impl.CourseDAO;
import ru.zhadaev.dao.repository.impl.GroupDAO;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.exception.IsNotFileException;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

@Component("schoolInitializer")
public class SchoolInitializer {
    ConnectionManager connectionManager;

    @Autowired
    public SchoolInitializer(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

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
                        .findFirst()             .get().getId();

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
        GroupDAO groupDAO = new GroupDAO(connectionManager);
        List<Group> groupsDb = new ArrayList<>();
        for (Group group : groups) {
            groupsDb.add(groupDAO.save(group));
        }

        return groupsDb;
    }

    List<Course> initializeCourses(List<Course> courses) throws DAOException {
        CourseDAO courseDAO = new CourseDAO(connectionManager);
        List<Course> coursesDb = new ArrayList<>();
        for (Course course : courses) {
            coursesDb.add(courseDAO.save(course));
        }

        return coursesDb;
    }

    List<Student> initializeStudents(List<Student> students) throws DAOException {
        List<Student> studentsDb = new ArrayList<>();
        StudentDAO studentDAO = new StudentDAO(connectionManager);
        for (Student student : students) {
            studentsDb.add(studentDAO.save(student));
        }

        return studentsDb;
    }

    void signStudentsOnCourses(List<Student> students) throws DAOException {
        StudentDAO studentDAO = new StudentDAO(connectionManager);
        for (Student student : students) {
            studentDAO.signOnCourses(student, student.getCourses());
        }
    }
}

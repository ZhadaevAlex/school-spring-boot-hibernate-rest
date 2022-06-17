package ru.zhadaev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.TablesCreator;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.School;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.impl.CourseDAO;
import ru.zhadaev.dao.repository.impl.GroupDAO;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.exception.IsNotFileException;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SchoolInitializer {
    private GroupDAO groupDAO;
    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private TablesCreator tablesCreator;

    @Autowired
    public SchoolInitializer(GroupDAO groupDAO,
                             CourseDAO courseDAO,
                             StudentDAO studentDAO,
                             TablesCreator tablesCreator) {
        this.groupDAO = groupDAO;
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
        this.tablesCreator = tablesCreator;
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
        tablesCreator.createTables();
    }

    List<Group> initializeGroup(List<Group> groups) throws DAOException {
        List<Group> groupsDb = new ArrayList<>();
        for (Group group : groups) {
            groupsDb.add(groupDAO.save(group));
        }

        return groupsDb;
    }

    List<Course> initializeCourses(List<Course> courses) throws DAOException {
        List<Course> coursesDb = new ArrayList<>();
        for (Course course : courses) {
            coursesDb.add(courseDAO.save(course));
        }

        return coursesDb;
    }

    List<Student> initializeStudents(List<Student> students) throws DAOException {
        List<Student> studentsDb = new ArrayList<>();
        for (Student student : students) {
            studentsDb.add(studentDAO.save(student));
        }

        return studentsDb;
    }

    void signStudentsOnCourses(List<Student> students) throws DAOException {
        for (Student student : students) {
            studentDAO.signOnCourses(student, student.getCourses());
        }
    }
}

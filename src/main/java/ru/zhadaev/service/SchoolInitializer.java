package ru.zhadaev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.TablesCreator;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.School;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.exception.IsNotFileException;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SchoolInitializer {
    private final GroupService groupService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final TablesCreator tablesCreator;

    @Autowired
    public SchoolInitializer(GroupService groupService,
                             CourseService courseService,
                             StudentService studentService,
                             TablesCreator tablesCreator) {
        this.groupService = groupService;
        this.courseService = courseService;
        this.studentService = studentService;
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
                        .findFirst().get().getId();

                courseStudent.setId(id);
            }
        }

        List<Student> studentsDb = initializeStudents(school.getStudents());

        signStudentsOnCourses(studentsDb);
    }

    private void createTables() throws DAOException, NoSuchFileException, IsNotFileException {
        tablesCreator.createTables();
    }

    private List<Group> initializeGroup(List<Group> groups) throws DAOException {
        List<Group> groupsDb = new ArrayList<>();
        for (Group group : groups) {
            groupsDb.add(groupService.save(group));
        }

        return groupsDb;
    }

    private List<Course> initializeCourses(List<Course> courses) throws DAOException {
        List<Course> coursesDb = new ArrayList<>();
        for (Course course : courses) {
            coursesDb.add(courseService.save(course));
        }

        return coursesDb;
    }

    private List<Student> initializeStudents(List<Student> students) throws DAOException {
        List<Student> studentsDb = new ArrayList<>();
        for (Student student : students) {
            studentsDb.add(studentService.save(student));
        }

        return studentsDb;
    }

    private void signStudentsOnCourses(List<Student> students) throws DAOException {

        for (Student student : students) {
            List<Integer> coursesId = new ArrayList<>();

            for (Course course : student.getCourses()) {
                coursesId.add(course.getId());
            }

            studentService.signOnCourses(student.getId(), coursesId);
        }
    }
}

package ru.zhadaev.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.impl.CourseDAO;
import ru.zhadaev.dao.repository.impl.GroupDAO;
import ru.zhadaev.dao.repository.impl.StudentDAO;
import ru.zhadaev.exception.*;
import ru.zhadaev.service.ISchoolManager;

import java.util.ArrayList;
import java.util.List;

@Component
public class SchoolManager implements ISchoolManager {
    private static final Logger logger = LoggerFactory.getLogger(SchoolManager.class);
    ConnectionManager connectionManager;

    @Autowired
    public SchoolManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Group> findGroupsByStudentsCount(long studentsCount) throws DAOException {
        StudentDAO studentDAO = new StudentDAO(connectionManager);
        GroupDAO groupDAO = new GroupDAO(connectionManager);

        List<Student> studentsDb = studentDAO.findAll();
        List<Group> groupsDb = groupDAO.findAll();
        List<Group> result = new ArrayList<>();

        for (Group group : groupsDb) {
            long countStudentsInGroup = studentsDb.stream()
                    .filter(p -> p.getGroup() != null && p.getGroup().getId().equals(group.getId()))
                    .count();

            if (countStudentsInGroup >= studentsCount) {
                result.add(group);
            }
        }

        return result;
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) throws DAOException {
        requiredNotNull(courseName);

        StudentDAO studentDAO = new StudentDAO(connectionManager);
        List<Student> studentsDb = studentDAO.findAll();
        List<Student> result = new ArrayList<>();

        for (Student student : studentsDb) {
            boolean contains = student.getCourses().stream()
                    .anyMatch(p -> p.getName().equals(courseName));

            if (contains) {
                result.add(student);
            }
        }

        return result;
    }

    @Override
    public List<Group> findGroup(Group group) throws DAOException {
        requiredNotNull(group);
        requiredNameNotNull(group);

        GroupDAO groupDAO = new GroupDAO(connectionManager);
        List<Group> groupsDb = groupDAO.find(group).get();

        if (groupsDb.isEmpty()) {
            throw new NotFoundException("Groups not found");
        }

        return groupsDb;
    }

    @Override
    public List<Course> findCourse(Course course) throws DAOException {
        requiredNotNull(course);
        requiredNameNotNull(course);

        CourseDAO courseDAO = new CourseDAO(connectionManager);
        List<Course> coursesDb = courseDAO.find(course).get();

        if (coursesDb.isEmpty()) {
            throw new NotFoundException("Courses not found");
        }

        return coursesDb;
    }

    @Override
    public List<Student> findStudent(Student student) throws DAOException {
        requiredNotNull(student);
        requiredNameNotNull(student);

        StudentDAO studentDAO = new StudentDAO(connectionManager);
        List<Student> studentsDb = studentDAO.find(student).get();
        if (studentsDb.isEmpty()) {
            throw new NotFoundException("Students not found");
        }

        return studentsDb;
    }

    @Override
    public Student addNewStudent(Student student) throws DAOException {
        requiredNotNull(student);
        requiredIdNotNull(student.getGroup());

        for (Course course : student.getCourses()) {
            requiredIdNotNull(course);
            requiredCourseIsExist(course);
        }

        StudentDAO studentDAO = new StudentDAO(connectionManager);

        Student studentDb = studentDAO.save(student);
        studentDAO.signOnCourses(studentDb, studentDb.getCourses());

        return studentDb;
    }

    @Override
    public void deleteStudentById(int id) throws DAOException {
        StudentDAO studentDAO = new StudentDAO(connectionManager);

        if (studentDAO.existsById(id)) {
            studentDAO.deleteById(id);
        } else {
            logger.error("Student not found by id");
            throw new NotFoundException("Student not found by id");
        }
    }

    @Override
    public void addStudentToCourses(Student student, List<Course> courses) throws DAOException {
        requiredNotNull(student);
        requiredIdNotNull(student);
        requiredStudentIsExist(student);

        for (Course course : courses) {
            requiredNotNull(course);
            requiredIdNotNull(course);
            requiredStudentIsExist(student);
        }

        StudentDAO studentDAO = new StudentDAO(connectionManager);
        studentDAO.signOnCourses(student, courses);
    }

    @Override
    public void removeStudentFromCourse(Student student, Course course) throws DAOException {
        requiredNotNull(student);
        requiredIdNotNull(student);
        requiredStudentIsExist(student);

        requiredNotNull(course);
        requiredIdNotNull(course);
        requiredStudentIsExist(student);

        StudentDAO studentDAO = new StudentDAO(connectionManager);
        studentDAO.removeFromCourse(student, course);
    }

    private void requiredNotNull(String courseName) {
        if (courseName == null) {
            logger.error("Course name required is not null!");
            throw new IllegalArgumentException("Course name required is not null!");
        }
    }

    private void requiredNotNull(Group group) {
        if (group == null) {
            logger.error("Group required is not null!");
            throw new NotValidGroupException("Group required is not null!");
        }
    }

    private void requiredNotNull(Course course) {
        if (course == null) {
            logger.error("Course required is not null!");
            throw new NotValidCourseException("Course required is not null!");
        }
    }

    private void requiredNotNull(Student student) {
        if (student == null) {
            logger.error("Student required is not null!");
            throw new NotValidStudentException("Student required is not null!");
        }
    }

    private void requiredIdNotNull(Group group) {
        if (group.getId() == null) {
            logger.error("Group ID required is not null");
            throw new NotValidStudentException("Group ID required is not null");
        }
    }

    private void requiredIdNotNull(Course course) {
        if (course.getId() == null) {
            logger.error("Course ID required is not null");
            throw new NotValidStudentException("Course ID required is not null");
        }
    }

    private void requiredIdNotNull(Student student) {
        if (student.getId() == null) {
            logger.error("Student ID required is not null!");
            throw new NotValidStudentException("Student ID required is not null!");
        }
    }

    private void requiredNameNotNull(Group group) {
        if (group.getName() == null) {
            logger.error("Group name required is not null!");
            throw new NotValidGroupException("Group name required is not null!");
        }
    }

    private void requiredNameNotNull(Course course) {
        if (course.getName() == null || course.getDescription() == null) {
            logger.error("Course name and course description required is not null!");
            throw new NotValidCourseException("Course name and course description required is not null!");
        }
    }

    private void requiredNameNotNull(Student student) {
        if (student.getFirstName() == null || student.getLastName() == null) {
            logger.error("Student name and surname required is not null!");
            throw new NotValidStudentException("Student name and surname required is not null!");
        }
    }

    private void requiredGroupIsExist(Student student) throws DAOException {
        GroupDAO groupDAO = new GroupDAO(connectionManager);
        if (!groupDAO.existsById(student.getGroup().getId())) {
            logger.error("The student's group was not found in the database");
            throw new NotFoundException("The student's group was not found in the database");
        }
    }

    private void requiredCourseIsExist(Course course) throws DAOException {
        CourseDAO courseDAO = new CourseDAO(connectionManager);
        if (!courseDAO.existsById(course.getId())) {
            logger.error("The student's course was not found in the database");
            throw new NotFoundException("The student's course was not found in the database");
        }
    }

    private void requiredStudentIsExist(Student student) throws DAOException {
        StudentDAO studentDAO = new StudentDAO(connectionManager);
        if (!studentDAO.existsById(student.getId())) {
            logger.error("The student's course was not found in the database");
            throw new NotFoundException("The student's was not found in the database");
        }
    }
}


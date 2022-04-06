package ru.zhadaev.dao.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.exception.DAOException;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class StudentRepository implements CrudRepository<Student, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(GroupRepository.class);
    private static final String STUDENT_ID = "student_id";
    private static final String CLOSE_ERROR_MSG = "ResultSet close error";
    private static final String CREATE_QUERY = "insert into school.students (group_id, first_name, last_name) values (?, ?, ?)";
    private static final String CREATE_STUDENTS_COURSES_QUERY = "insert into school.students_courses (student_id, course_id) values (?, ?)";

    private final ConnectionManager connectionManager;

    public StudentRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Student save(Student entity) throws DAOException {
        Student student;
        Group group = null;
        Set<Course> courses = new HashSet<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        GroupRepository groupRepository = new GroupRepository(this.connectionManager);
        if (entity.getGroup() != null) {
            boolean existsGroup = groupRepository.existsById(entity.getGroup().getId());
            if (!existsGroup) {
                group = groupRepository.save(entity.getGroup());
            } else {
                group = entity.getGroup();
            }
        }

        CourseRepository courseRepository = new CourseRepository(this.connectionManager);
        for (Course course : entity.getCourses()) {
            boolean existsCourse = courseRepository.existsById(course.getId());
            if (!existsCourse) {
                courses.add(courseRepository.save(course));
            } else {
                courses.add(course);
            }
        }

        try (PreparedStatement preStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            if (entity.getGroup() == null) {
                preStatement.setNull(1, Types.INTEGER);
            } else {
                preStatement.setInt(1, entity.getGroup().getId());
            }
            preStatement.setString(2, entity.getFirstName());
            preStatement.setString(3, entity.getLastName());
            preStatement.execute();

            resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            student = new Student(entity.getFirstName(), entity.getLastName());
            student.setId(resultSet.getInt(STUDENT_ID));
            student.setGroup(group);
            student.setCourses(courses);
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot save the student", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        saveStudentsCourses(student);

        return student;
    }

    @Override
    public Optional<Student> findById(Integer integer) throws DAOException {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) throws DAOException {
        return false;
    }

    @Override
    public List<Student> findAll() throws DAOException {
        return null;
    }

    @Override
    public long count() throws DAOException {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) throws DAOException {

    }

    @Override
    public void delete(Student entity) throws DAOException {

    }

    @Override
    public void deleteAll() throws DAOException {

    }

    private void saveStudentsCourses(Student entity) throws DAOException {
        Student student;
        Group group = null;
        Set<Course> courses = new HashSet<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        for (Course course : entity.getCourses()) {
            try (PreparedStatement preStatement = connection.prepareStatement(CREATE_STUDENTS_COURSES_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preStatement.setInt(1, entity.getId());
                preStatement.setInt(2, course.getId());
                preStatement.execute();
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
                throw new DAOException("Cannot save the student", e);
            } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (SQLException e) {
                    logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
                }
            }
        }
    }
}

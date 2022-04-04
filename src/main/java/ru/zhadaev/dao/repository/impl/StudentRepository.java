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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository implements CrudRepository<Student, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(GroupRepository.class);
    private static final String STUDENT_ID = "student_id";
    private static final String GROUP_ID = "group_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String CLOSE_ERROR_MSG = "ResultSet close error";

    private final ConnectionManager connectionManager;

    public StudentRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Student save(Student entity) throws DAOException {
        Student student;
        Group group;
        List<Course> courses = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        GroupRepository groupRepository = new GroupRepository(this.connectionManager);
        boolean existsGroup = groupRepository.existsById(entity.getGroup().getId());
        if (!existsGroup) {
            group = groupRepository.save(entity.getGroup());
        } else {
            group = entity.getGroup();
        }

        CourseRepository courseRepository = new CourseRepository(this.connectionManager);
        for (Course course: entity.getCourse()) {
            boolean existsCourse = courseRepository.existsById(course.getId());
            if (!existsCourse) {
                courses.add(courseRepository.save(course));
            } else {
                courses.add(course);
            }
        }

        try (PreparedStatement preStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, entity.getName());
            preStatement.execute();

            resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            group = new Group(resultSet.getString(GROUP_NAME));
            group.setId(resultSet.getInt(GROUP_ID));
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot save the group", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        //try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE))
        return new Student("", "");
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
}

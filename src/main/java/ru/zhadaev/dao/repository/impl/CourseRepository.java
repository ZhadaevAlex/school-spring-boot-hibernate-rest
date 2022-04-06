package ru.zhadaev.dao.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepository implements CrudRepository<Course, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(CourseRepository.class);
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_DESCRIPTION = "course_description";
    private static final String CLOSE_ERROR_MSG = "ResultSet close error";
    private static final String CREATE_QUERY = "insert into school.courses (course_name, course_description) values (?, ?)";
    private static final String FIND_BY_ID_QUERY = "select * from school.courses where course_id = ?";
    private static final String FIND_ALL_QUERY = "select * from school.courses";
    private static final String COUNT_QUERY = "select count(*) from school.courses";
    private static final String DELETE_BY_ID_QUERY = "delete from school.courses where course_id = ?";
    private static final String DELETE_ALL = "truncate school.courses restart identity cascade";

    private final ConnectionManager connectionManager;

    public CourseRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Course save(Course entity) throws DAOException {
        Course course;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, entity.getName());
            preStatement.setString(2, entity.getDescription());
            preStatement.execute();

            resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            course = new Course(entity.getName());
            course.setId(resultSet.getInt(COURSE_ID));
            course.setDescription(entity.getDescription());
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot save the course", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return course;
    }

    @Override
    public Optional<Course> findById(Integer integer) throws DAOException {
        Course course = null;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(FIND_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, integer);
            resultSet = preStatement.executeQuery();

            if (resultSet.next()) {
                course = new Course(resultSet.getString(COURSE_NAME));
                course.setId(resultSet.getInt(COURSE_ID));
                course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("cannot be found by id in the courses", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return Optional.ofNullable(course);
    }

    @Override
    public boolean existsById(Integer integer) throws DAOException {
        Optional<Course> optCourse = this.findById(integer);

        return optCourse.isPresent();
    }

    @Override
    public List<Course> findAll() throws DAOException {
        List<Course> courses = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(FIND_ALL_QUERY);

            while (resultSet.next()) {
                Course course = new Course(resultSet.getString(COURSE_NAME));
                course.setId(resultSet.getInt(COURSE_ID));
                course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                courses.add(course);
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be found all in the courses", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return courses;
    }

    @Override
    public long count() throws DAOException {
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(COUNT_QUERY);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DAOException("Cannot to count a courses");
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot to count a courses", e);
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

    @Override
    public void deleteById(Integer integer) throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, integer);
            preStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted by id in the courses", e);
        }
    }

    @Override
    public void delete(Course entity) throws DAOException {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAll() throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute(DELETE_ALL);
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted all in the courses", e);
        }
    }
}

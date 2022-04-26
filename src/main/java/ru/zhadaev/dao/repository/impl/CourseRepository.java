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
    private static final String CREATE_QUERY = "insert into school.courses (course_name, course_description) values (?, ?)";
    private static final String FIND_BY_ID_QUERY = "select * from school.courses where course_id = ?";
    private static final String FIND_QUERY = "select * from school.courses where" +
            " course_name = ? AND" +
            " course_description = ?";
    private static final String FIND_ALL_QUERY = "select * from school.courses";
    private static final String COUNT_QUERY = "select count(*) from school.courses";
    private static final String DELETE_BY_ID_QUERY = "delete from school.courses where course_id = ?";
    private static final String DELETE_QUERY = "delete from school.courses where" +
            " course_name = ? AND" +
            " course_description = ?";
    private static final String DELETE_ALL_QUERY = "delete from school.courses";
    private static final String CLOSE_ERROR_MSG = "ResultSet close error";

    private final ConnectionManager connectionManager;

    public CourseRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Course save(Course course) {
        Course courseDb;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, course.getName());
            preStatement.setString(2, course.getDescription());
            preStatement.execute();

            resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            courseDb = new Course(course.getName());
            courseDb.setId(resultSet.getInt(COURSE_ID));
            courseDb.setDescription(course.getDescription());
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

        return courseDb;
    }

    @Override
    public Optional<Course> findById(Integer id) {
        Course courseDb = null;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(FIND_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, id);

            resultSet = preStatement.executeQuery();
            if (resultSet.next()) {
                courseDb = new Course(resultSet.getString(COURSE_NAME));
                courseDb.setId(resultSet.getInt(COURSE_ID));
                courseDb.setDescription(resultSet.getString(COURSE_DESCRIPTION));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be found by id in the courses", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return Optional.ofNullable(courseDb);
    }

    @Override
    public List<Course> find(Course course) {
        List<Course> coursesDb = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(FIND_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, course.getName());
            preStatement.setString(2, course.getDescription());
            preStatement.getGeneratedKeys();
            preStatement.execute();

            resultSet = preStatement.getResultSet();
            while(resultSet.next()) {
                Course courseDb = new Course(resultSet.getString(COURSE_NAME));
                courseDb.setId(resultSet.getInt(COURSE_ID));
                courseDb.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                coursesDb.add(courseDb);
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("The group cannot be found in groups", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        return coursesDb;
    }

    @Override
    public List<Course> findAll() {
        List<Course> coursesDb = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(FIND_ALL_QUERY);
            while (resultSet.next()) {
                Course courseResult = new Course(resultSet.getString(COURSE_NAME));
                courseResult.setId(resultSet.getInt(COURSE_ID));
                courseResult.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                coursesDb.add(courseResult);
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

        return coursesDb;
    }

    @Override
    public boolean existsById(Integer id) {
        Optional<Course> optCourse = this.findById(id);

        return optCourse.isPresent();
    }

    @Override
    public long count() {
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
    public void deleteById(Integer id) {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, id);
            preStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted by id in the courses", e);
        }
    }

    @Override
    public void delete(Course course) {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, course.getName());
            preStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted in the groups", e);
        }
    }

    @Override
    public void deleteAll() {
        Connection connection = connectionManager.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute(DELETE_ALL_QUERY);
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted all in the courses", e);
        }
    }
}

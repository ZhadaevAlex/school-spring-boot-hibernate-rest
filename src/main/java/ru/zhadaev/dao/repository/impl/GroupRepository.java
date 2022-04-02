package ru.zhadaev.dao.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupRepository implements CrudRepository<Group, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(GroupRepository.class);
    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";
    private static final String CLOSE_ERROR_MSG = "ResultSet close error";
    private static final String CREATE_QUERY = "insert into school.groups (group_name) values (?)";
    private static final String FIND_BY_ID_QUERY = "select * from school.groups where group_id = ?";
    private static final String FIND_ALL_QUERY = "select * from school.groups";
    private static final String COUNT_QUERY = "select count(*) from school.groups";
    private static final String DELETE_BY_ID_QUERY = "delete from school.groups where group_id = ?";
    private static final String DELETE_ALL = "delete from school.groups";

    private final ConnectionManager connectionManager;

    public GroupRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Group save(Group entity) throws DAOException {
        Group group;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

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

        return group;
    }

    @Override
    public Optional<Group> findById(Integer integer) throws DAOException {
        Group group = null;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(FIND_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, integer);
            resultSet = preStatement.executeQuery();

            if (resultSet.next()) {
                group = new Group(resultSet.getString(GROUP_NAME));
                group.setId(resultSet.getInt(GROUP_ID));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("cannot be found by id in the group", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return Optional.ofNullable(group);
    }

    @Override
    public boolean existsById(Integer integer) throws DAOException {
        Optional<Group> optGroup = this.findById(integer);

        return optGroup.isPresent();
    }

    @Override
    public List<Group> findAll() throws DAOException {
        List<Group> groups = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(FIND_ALL_QUERY);

            while (resultSet.next()) {
                Group group = new Group(resultSet.getString(GROUP_NAME));
                group.setId(resultSet.getInt(GROUP_ID));
                groups.add(group);
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be found all in the group", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return groups;
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
                throw new DAOException("Cannot to count a groups");
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot to count a groups", e);
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
            throw new DAOException("Cannot be deleted by id in the group", e);
        }
    }

    @Override
    public void delete(Group entity) throws DAOException {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAll() throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute(DELETE_ALL);
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted all in the group", e);
        }
    }
}

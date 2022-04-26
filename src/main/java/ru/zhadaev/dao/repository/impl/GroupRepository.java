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
    private static final String CREATE_QUERY = "insert into school.groups (group_name) values (?)";
    private static final String FIND_BY_ID_QUERY = "select * from school.groups where group_id = ?";
    private static final String FIND_QUERY = "select * from school.groups where group_name = ?";
    private static final String FIND_ALL_QUERY = "select * from school.groups";
    private static final String COUNT_QUERY = "select count(*) from school.groups";
    private static final String DELETE_BY_ID_QUERY = "delete from school.groups where group_id = ?";
    private static final String DELETE_QUERY = "delete from school.groups where group_name = ?";
    private static final String DELETE_ALL_QUERY = "delete from school.groups";
    private static final String CLOSE_ERROR_MSG = "ResultSet close error";

    private final ConnectionManager connectionManager;

    public GroupRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Group save(Group group) throws DAOException {
        Group groupDb;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, group.getName());
            preStatement.execute();

            resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            groupDb = new Group(group.getName());
            groupDb.setId(resultSet.getInt(GROUP_ID));
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

        return groupDb;
    }

    @Override
    public Optional<Group> findById(Integer id) throws DAOException {
        Group groupDb = null;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(FIND_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, id);

            resultSet = preStatement.executeQuery();
            if (resultSet.next()) {
                groupDb = new Group(resultSet.getString(GROUP_NAME));
                groupDb.setId(resultSet.getInt(GROUP_ID));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be found by id in the groups", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return Optional.ofNullable(groupDb);
    }

    @Override
    public List<Group> find(Group group) throws DAOException {
        List<Group> groupsDb = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(FIND_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, group.getName());
            preStatement.getGeneratedKeys();
            preStatement.execute();

            resultSet = preStatement.getResultSet();
            while (resultSet.next()) {
                Group groupDb = new Group(resultSet.getString(GROUP_NAME));
                groupDb.setId(resultSet.getInt(GROUP_ID));
                groupsDb.add(groupDb);
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
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return groupsDb;
    }

    @Override
    public List<Group> findAll() throws DAOException {
        List<Group> groupsDb = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(FIND_ALL_QUERY);
            while (resultSet.next()) {
                Group groupDb = new Group(resultSet.getString(GROUP_NAME));
                groupDb.setId(resultSet.getInt(GROUP_ID));
                groupsDb.add(groupDb);
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("All groups in groups cannot be found", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return groupsDb;
    }

    @Override
    public boolean existsById(Integer id) throws DAOException {
        Optional<Group> optGroup = this.findById(id);

        return optGroup.isPresent();
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
    public void deleteById(Integer id) throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, id);
            preStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted by id in the groups", e);
        }
    }

    @Override
    public void delete(Group group) throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, group.getName());
            preStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted in the groups", e);
        }
    }

    @Override
    public void deleteAll() throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute(DELETE_ALL_QUERY);
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted all in the groups", e);
        }
    }
}

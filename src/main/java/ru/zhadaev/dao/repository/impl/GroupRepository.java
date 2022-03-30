package ru.zhadaev.dao.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.exception.DAOException;
import ru.zhadaev.config.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupRepository implements CrudRepository<Group, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(GroupRepository.class);
    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";
    private static final String MSG_SQL_DELETE = "The DELETE operation violates the foreign key constraint of the students table";
    private static final String CREATE_QUERY = "insert into school.groups (group_name) values (?)";
    private static final String DELETE_QUERY = "select * from school.groups where group_id = ?";
    private static final String FIND_ALL_QUERY = "select * from school.groups";
    private static final String COUNT_QUERY = "select count(*) from school.groups;";

    private final ConnectionManager connectionManager;

    public GroupRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Group save(Group entity) throws SQLException {
        Group group;
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, entity.getName());
            preStatement.execute();

            ResultSet resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            group = new Group(resultSet.getString(GROUP_NAME));
            group.setId(resultSet.getInt(GROUP_ID));
            return group;
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot save the group", e);
        }
    }

    @Override
    public Optional<Group> findById(Integer integer) throws DAOException {
        Connection connection = connectionManager.getConnection();

        Group group = null;
        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, integer);
            ResultSet resultSet = preStatement.executeQuery();

            if (resultSet.next()) {
                group = new Group(resultSet.getString(GROUP_NAME));
                group.setId(resultSet.getInt(GROUP_ID));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("cannot be found by id in the group", e);
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

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY);

            while (resultSet.next()) {
                Group group = new Group(resultSet.getString(GROUP_NAME));
                group.setId(resultSet.getInt(GROUP_ID));
                groups.add(group);
            }
            return groups;
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be found all in the group", e);
        }
    }

    @Override
    public long count() throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(COUNT_QUERY);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else throw new DAOException("Cannot count groups");
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot to count the number in the table", e);
        }
    }

    @Override
    public void deleteById(Integer integer) {
        logger.error(MSG_SQL_DELETE); // ToDo -> to implement!
    }

    @Override
    public void delete(Group entity) {
        logger.error(MSG_SQL_DELETE); // ToDo -> to implement!
    }

    @Override
    public void deleteAll() {
        logger.error(MSG_SQL_DELETE); // ToDo -> to implement!
    }
}

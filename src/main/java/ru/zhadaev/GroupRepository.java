package ru.zhadaev;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;

public class GroupRepository implements CrudRepository<Group, Integer> {
    ConnectionManager conManager = ConnectionManager.getInstance();
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public Group save(Group entity) throws SQLException {
        Group group;
        Connection connection = conManager.getConnection();
        ResultSet resultSet = null;
        PreparedStatement preStatement = null;

        String sql = "insert into school.groups (group_name) values (?)";

        try {
            preStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preStatement.setString(1, entity.getName());
            preStatement.execute();

            resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            group = new Group(resultSet.getString("group_name"));
            group.setId(resultSet.getInt("group_id"));
        } catch (SQLException e) {
            logger.error("Cannot save the group", e);
            throw new DAOException("Cannot save the group", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preStatement != null) {
                    preStatement.close();
                }

                connection.close();
            } catch (SQLException e) {
                logger.error("Close error", e);
                throw new DAOException("Cannot save the group", e);
            }
        }

        if (group == null) {
            group = new Group(new String());
        }

        return group;
    }

    @Override
    public Optional<Group> findById(Integer integer) throws DAOException {
        Group group = null;
        Connection connection = conManager.getConnection();
        ResultSet resultSet = null;
        PreparedStatement preStatement = null;

        String sql = "select * from school.groups where group_id = ?;";

        try {
            preStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preStatement.setInt(1, integer);
            resultSet = preStatement.executeQuery();

            //resultSet = preStatement.getGeneratedKeys();
            if (resultSet.next()) {
                group = new Group(resultSet.getString("group_name"));
                group.setId(resultSet.getInt("group_id"));
            }
        } catch (SQLException e) {
            logger.error("Cannot save the group", e);
            throw new DAOException("Cannot save the group", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preStatement != null) {
                    preStatement.close();
                }

                connection.close();
            } catch (SQLException e) {
                logger.error("Close error", e);
                throw new DAOException("Cannot save the group", e);
            }
        }

        return Optional.ofNullable(group);
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<Group> findAll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Group entity) {

    }

    @Override
    public void deleteAll() {

    }
}

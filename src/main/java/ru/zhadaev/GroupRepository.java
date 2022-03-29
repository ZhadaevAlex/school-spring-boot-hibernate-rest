package ru.zhadaev;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;

public class GroupRepository implements CrudRepository<Group, Integer> {
    ConnectionManager conManager = ConnectionManager.getInstance();
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public Group save(Group entity) throws DAOException {
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
                throw new DAOException("Close error", e);
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

        String sql = "select * from school.groups where group_id = ?";

        try {
            preStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preStatement.setInt(1, integer);
            resultSet = preStatement.executeQuery();

            if (resultSet.next()) {
                group = new Group(resultSet.getString("group_name"));
                group.setId(resultSet.getInt("group_id"));
            }
        } catch (SQLException e) {
            logger.error("Cannot be found by id in the group", e);
            throw new DAOException("cannot be found by id in the group", e);
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
                throw new DAOException("Close error", e);
            }
        }

        return Optional.ofNullable(group);
    }

    @Override
    public boolean existsById(Integer integer) throws DAOException {
        Optional<Group> optGroup = this.findById(integer);

        if (optGroup.isPresent()) {
            return true;
        }

        return false;
    }

    @Override
    public List<Group> findAll() throws DAOException {
        List<Group> groups = new ArrayList<>();
        Connection connection = conManager.getConnection();
        ResultSet resultSet = null;
        Statement statement = null;

        String sql = "select * from school.groups";

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Group group = new Group(resultSet.getString("group_name"));
                group.setId(resultSet.getInt("group_id"));
                groups.add(group);
            }
        } catch (SQLException e) {
            logger.error("Cannot be found all in the group", e);
            throw new DAOException("Cannot be found all in the group", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (statement != null) {
                    statement.close();
                }

                connection.close();
            } catch (SQLException e) {
                logger.error("Close error", e);
                throw new DAOException("Close error", e);
            }
        }

        return groups;
    }

    @Override
    public long count() throws DAOException {
        Connection connection = conManager.getConnection();
        ResultSet resultSet = null;
        Statement statement = null;

        String sql = "select count(*) from school.groups;";

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            return 0;
        } catch (SQLException e) {
            logger.error("Cannot to count the number in the table", e);
            throw new DAOException("Cannot to count the number in the table", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (statement != null) {
                    statement.close();
                }

                connection.close();
            } catch (SQLException e) {
                logger.error("Close error", e);
                throw new DAOException("Close error", e);
            }
        }
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

package ru.zhadaev.dao.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements RowMapper<Group> {
    private static final Logger logger = LoggerFactory.getLogger(GroupMapper.class);
    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";

    @Override
    public Group mapRow(ResultSet resultSet, int i) {
        Group group = new Group();

        try {
            group.setId(resultSet.getInt(GROUP_ID));
            group.setName(resultSet.getString(GROUP_NAME));
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Mapper error when mapping a group", e);
        }

        return group;
    }
}

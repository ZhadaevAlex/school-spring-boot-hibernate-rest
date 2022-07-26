package ru.zhadaev.dao.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper implements RowMapper<Course> {
    private static final Logger logger = LoggerFactory.getLogger(CourseMapper.class);
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_DESCRIPTION = "course_description";

    @Override
    public Course mapRow(ResultSet resultSet, int i) {
        Course course = new Course();

        try {
            course.setId(resultSet.getInt(COURSE_ID));
            course.setName(resultSet.getString(COURSE_NAME));
            course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Mapper error when mapping a group", e);
        }

        return course;
    }
}

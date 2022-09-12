package ru.zhadaev.dao.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.mappers.CourseMapper;
import ru.zhadaev.dao.repository.CrudRepository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Component
public class CourseDAO implements CrudRepository<Course, Integer> {
    private static final String COURSE_ID = "course_id";
    private static final String CREATE_QUERY = "insert into school.courses (course_name, course_description) values (?, ?)";
    private static final String UPDATE_QUERY = "update school.courses set (course_name, course_description) = (?, ?) where course_id = ?";
    private static final String FIND_BY_ID_QUERY = "select * from school.courses where course_id = ?";
    private static final String FIND_QUERY = "select * from school.courses where" +
            " course_name = ?";
    private static final String FIND_ALL_QUERY = "select * from school.courses order by course_id";
    private static final String COUNT_QUERY = "select count(*) from school.courses";
    private static final String DELETE_BY_ID_QUERY = "delete from school.courses where course_id = ?";
    private static final String DELETE_QUERY = "delete from school.courses where" +
            " course_name = ? AND" +
            " course_description = ?";
    private static final String DELETE_ALL_QUERY = "delete from school.courses";
    private static final Integer INTEGER = 4;
    private static final Integer VARCHAR = 12;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Course save(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(CREATE_QUERY, new String[] {COURSE_ID});
                    ps.setString(1, course.getName());
                    ps.setString(2, course.getDescription());
                    return ps;
                }, keyHolder);
        int id = keyHolder.getKey().intValue();
        course.setId(id);
        return course;
    }

    @Override
    public Course update(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY, new String[] {COURSE_ID});
                    ps.setString(1, course.getName());
                    ps.setString(2, course.getDescription());
                    ps.setInt(3, course.getId());
                    return ps;
                }, keyHolder);
        int id = keyHolder.getKey().intValue();
        course.setId(id);
        return course;
    }

    @Override
    public Optional<Course> findById(Integer id) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, new Object[]{id}, new int[]{INTEGER}, new CourseMapper()).stream().findAny();
    }

    @Override
    public List<Course> findLike(Course course) {
        return jdbcTemplate.query(FIND_QUERY, new Object[]{course.getName()}, new int[]{VARCHAR}, new CourseMapper());
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new CourseMapper());
    }

    @Override
    public boolean existsById(Integer id) {
        Optional<Course> optCourse = this.findById(id);

        return optCourse.isPresent();
    }

    @Override
    public long count() {
        return this.jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class);
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }

    @Override
    public void delete(Course course) {
        jdbcTemplate.update(DELETE_QUERY, course.getName());
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_QUERY);
    }
}

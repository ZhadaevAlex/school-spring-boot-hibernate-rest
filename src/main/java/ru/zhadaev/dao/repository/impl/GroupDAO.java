package ru.zhadaev.dao.repository.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.mappers.GroupMapper;
import ru.zhadaev.dao.repository.CrudRepository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class GroupDAO implements CrudRepository<Group, Integer> {
    private static final String GROUP_ID = "group_id";
    private static final String CREATE_QUERY = "insert into school.groups (group_name) values (?)";
    private static final String UPDATE_QUERY = "update school.groups set group_name = ? where group_id = ?";
    private static final String FIND_BY_ID_QUERY = "select * from school.groups where group_id = ?";
    private static final String FIND_QUERY = "select * from school.groups where group_name = ?";
    private static final String FIND_ALL_QUERY = "select * from school.groups order by group_id";
    private static final String COUNT_QUERY = "select count(*) from school.groups";
    private static final String DELETE_BY_ID_QUERY = "delete from school.groups where group_id = ?";
    private static final String DELETE_QUERY = "delete from school.groups where group_name = ?";
    private static final String DELETE_ALL_QUERY = "delete from school.groups";
    private static final Integer INTEGER = 4;
    private static final Integer VARCHAR = 12;

    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public GroupDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Group save(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(CREATE_QUERY, new String[]{GROUP_ID});
                    ps.setString(1, group.getName());
                    return ps;
                }, keyHolder);
        int id = extractId(keyHolder);
        group.setId(id);
        return group;
    }

    @Override
    public Group update(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY, new String[]{GROUP_ID});
                    ps.setString(1, group.getName());
                    ps.setInt(2, group.getId());
                    return ps;
                }, keyHolder);
        int id = extractId(keyHolder);
        group.setId(id);
        return group;
    }

    @Override
    public Optional<Group> findById(Integer id) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, new Object[]{id}, new int[]{INTEGER}, new GroupMapper()).stream().findAny();
    }

    @Override
    public Optional<List<Group>> find(Group group) {
        return Optional.of(jdbcTemplate.query(FIND_QUERY, new Object[]{group.getName()}, new int[]{VARCHAR}, new GroupMapper()));
    }

    @Override
    public List<Group> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new GroupMapper());
    }

    @Override
    public boolean existsById(Integer id) {
        Optional<Group> optGroup = this.findById(id);

        return optGroup.isPresent();
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
    public void delete(Group group) {
        jdbcTemplate.update(DELETE_QUERY, group.getName());
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_QUERY);
    }
}

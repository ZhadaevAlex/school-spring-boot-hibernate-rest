package ru.zhadaev.dao.repository.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class GroupDAO implements CrudRepository<Group, UUID> {
    private final SessionFactory sessionFactory;

    @Override
    public Group save(Group group) {
        getSession().save(group);
        return group;
    }

    @Override
    public Group update(Group group) {
        getSession().update(group);
        return group;
    }

    @Override
    public Optional<Group> findById(UUID id) {
        return ofNullable(getSession()
                .get(Group.class, id));
    }

    @Override
    public List<Group> findLike(Group group) {
        Query<Group> query = getSession().createQuery("from Group where name = :name", Group.class);
        query.setParameter("name", group.getName());
        return query.list();
    }

    @Override
    public List<Group> findAll() {
        return getSession()
                .createQuery("from Group", Group.class)
                .list();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.findById(id).isPresent();
    }

    @Override
    public long count() {
        return (long) getSession().createQuery("select count(*) from Group").getSingleResult();
    }

    @Override
    public void deleteById(UUID id) {
        Query query = getSession().createQuery("delete from Group where id =:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void delete(Group group) {
        deleteById(group.getId());
    }

    @Override
    public void deleteAll() {
        getSession().createQuery("delete from Group").executeUpdate();
    }

    protected final Session getSession() {
        return sessionFactory.isClosed() ? sessionFactory.openSession() : sessionFactory.getCurrentSession();
    }
}

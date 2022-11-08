package ru.zhadaev.dao.repository.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class CourseDAO implements CrudRepository<Course, UUID> {
    private final SessionFactory sessionFactory;

    @Override
    public Course save(Course course) {
        getSession().save(course);
        return course;
    }

    @Override
    public Course update(Course course) {
        getSession().update(course);
        return course;
    }

    @Override
    public Optional<Course> findById(UUID id) {
        return ofNullable(getSession()
                .get(Course.class, id));
    }

    @Override
    public List<Course> findLike(Course course) {
        Query<Course> query = getSession().createQuery("from Course where name = :name", Course.class);
        query.setParameter("name", course.getName());
        return query.list();
    }

    @Override
    public List<Course> findAll() {
        return getSession()
                .createQuery("from Course", Course.class)
                .list();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.findById(id).isPresent();
    }

    @Override
    public long count() {
        return (long) getSession().createQuery("select count(*) from Course").getSingleResult();
    }

    @Override
    public void deleteById(UUID id) {
        Query query = getSession().createQuery("delete from Course where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void delete(Course course) {
        deleteById(course.getId());
    }

    @Override
    public void deleteAll() {
        getSession().createQuery("delete from Course").executeUpdate();
    }

    private Session getSession() {
        return sessionFactory.isClosed() ? sessionFactory.openSession() : sessionFactory.getCurrentSession();
    }
}

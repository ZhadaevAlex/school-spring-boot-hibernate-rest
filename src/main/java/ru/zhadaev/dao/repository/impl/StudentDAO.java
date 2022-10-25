package ru.zhadaev.dao.repository.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class StudentDAO implements CrudRepository<Student, Integer> {
    private final SessionFactory sessionFactory;

    @Override
    public Student save(Student student) {
        getSession().save(student);
        return student;
    }

    @Override
    public Student update(Student student) {
        getSession().update(student);
        return student;
    }

    @Override
    public Optional<Student> findById(Integer id) {
        return ofNullable(getSession()
                .get(Student.class, id));
    }

    @Override
    public List<Student> findLike(Student student) {
        Query<Student> query = getSession().createQuery("from Student where firstName = :firstName AND lastName = :lastName", Student.class);
        query.setParameter("firstName", student.getFirstName());
        query.setParameter("lastName", student.getLastName());
        return query.list();
    }

    @Override
    public List<Student> findAll() {
        return getSession().createQuery("from Student", Student.class).list();
    }

    @Override
    public boolean existsById(Integer id) {
        Optional<Student> optStudent = this.findById(id);

        return optStudent.isPresent();
    }

    @Override
    public long count() {
        return (long) getSession().createQuery("select count(*) from Student").getSingleResult();
    }

    @Override
    public void deleteById(Integer id) {
        Query query = getSession().createQuery("delete from Student where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void delete(Student student) {
        deleteById(student.getId());
    }

    @Override
    public void deleteAll() {
        getSession().createQuery("delete from Student ").executeUpdate();
    }

    private Session getSession() {
        return sessionFactory.isClosed() ? sessionFactory.openSession() : sessionFactory.getCurrentSession();
    }
}

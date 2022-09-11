package ru.zhadaev.dao.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Component
public class GroupDAO implements CrudRepository<Group, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(GroupDAO.class);
    private final SessionFactory sessionFactory;

    @Autowired
    public GroupDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Group save(Group group) {
        getCurrentSession().save(group);
        //hibernateTemplate.save(group);
//        Session session = null;
//        Transaction transaction = null;
//
//        try {
//            session = sessionFactory.openSession();
//            transaction = session.getTransaction();
//            transaction.begin();
//            session.save(group);
//            transaction.commit();
//        } catch (Exception ex) {
//            transactionRollback(transaction);
//            logger.error("Save group error");
//            throw ex;
//        } finally {
//            sessionClose(session);
//        }
//
        return group;
    }

    @Override
    public Group update(Group group) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.getTransaction();
            transaction.begin();
            session.update(group);
            transaction.commit();
        } catch (Exception ex) {
            transactionRollback(transaction);
            logger.error("Update group error");
            throw ex;
        } finally {
            sessionClose(session);
        }

        return group;
    }

    @Override
    public Optional<Group> findById(Integer id) {
        Optional<Group> groupOpt;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            groupOpt = Optional.ofNullable(session.get(Group.class, id));
        } catch (Exception ex) {
            logger.error("Find group by id error");
            throw ex;
        } finally {
            sessionClose(session);
        }

        return groupOpt;
    }

    @Override
    public Optional<List<Group>> findLike(Group group) {
        Session session = null;
        Optional<List<Group>> groupsOpt;

        try {
            session = sessionFactory.openSession();
            Query<Group> query = session.createQuery("from Group where name = :name", Group.class);
            query.setParameter("name", group.getName());
            groupsOpt = Optional.of(query.list());
        } catch (Exception ex) {
            logger.error("Find group error");
            throw ex;
        } finally {
            sessionClose(session);
        }

        return groupsOpt;
    }

    @Override
    public List<Group> findAll() {
        Session session = null;
        List<Group> groups;

        try {
            session = sessionFactory.openSession();
            groups = session.createQuery("from Group", Group.class).list();
        } catch (Exception ex) {
            logger.error("Find all groups error");
            throw ex;
        } finally {
            sessionClose(session);
        }

        return groups;
    }

    @Override
    public boolean existsById(Integer id) {
        Optional<Group> optGroup = this.findById(id);

        return optGroup.isPresent();
    }

    @Override
    public long count() {
        Session session = null;
        Long result;

        try {
            session = sessionFactory.openSession();
            result = (Long) session.createQuery("select count(*) from Group").getSingleResult();
        } catch (Exception ex) {
            logger.error("Error counting rows");
            throw ex;
        } finally {
            sessionClose(session);
        }

        return result;
    }

    @Override
    public void deleteById(Integer id) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.getTransaction();
            transaction.begin();
            Group group = new Group();
            group.setId(id);
            session.delete(group);
            transaction.commit();
        } catch (Exception ex) {
            transactionRollback(transaction);
            logger.error("Delete by id error");
            throw ex;
        } finally {
            sessionClose(session);
        }
    }

    @Override
    public void delete(Group group) {
        deleteById(group.getId());
    }

    @Override
    public void deleteAll() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            Query<Group> query = session.createQuery("delete from Group", Group.class);
            transaction = session.getTransaction();
            transaction.begin();
            query.executeUpdate();
            transaction.commit();
        } catch (Exception ex) {
            transactionRollback(transaction);
            logger.error("Delete all groups error");
            throw ex;
        } finally {
            sessionClose(session);
        }
    }

    private void transactionRollback(Transaction transaction) {
        if (transaction != null) {
            transaction.rollback();
        }
    }

    private void sessionClose(Session session) {
        if (session != null) {
            session.close();
        }
    }

    private void exc() {
        throw new RuntimeException();
    }

    protected final Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }
}

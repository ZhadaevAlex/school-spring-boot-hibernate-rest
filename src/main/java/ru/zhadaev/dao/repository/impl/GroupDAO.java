package ru.zhadaev.dao.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.getTransaction();
            transaction.begin();
            session.save(group);
            transaction.commit();
        } catch (Exception ex) {
            transactionRollback(transaction);
            logger.error("Save group error");
            throw ex;
        } finally {
            sessionClose(session);
        }

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
    public Optional<List<Group>> find(Group group) {
        Session session = null;
        Optional<List<Group>> groupsOpt;

        try {
            session = sessionFactory.openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);
            Root<Group> root = criteriaQuery.from(Group.class);
            criteriaQuery.select(root).where(criteriaBuilder.like(root.get("name"), group.getName()));
            Query<Group> query = session.createQuery(criteriaQuery);
            groupsOpt = Optional.of(query.getResultList());
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
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);
            Root<Group> root = criteriaQuery.from(Group.class);
            criteriaQuery.select(root);
            Query<Group> query = session.createQuery(criteriaQuery);
            groups = query.getResultList();
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
            Criteria criteria = session.createCriteria(Group.class);
            criteria.setProjection(Projections.rowCount());
            result = (Long) criteria.uniqueResult();
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
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaDelete<Group> criteriaDelete = criteriaBuilder.createCriteriaDelete(Group.class);
            Root<Group> root = criteriaDelete.from(Group.class);
            criteriaDelete.where(criteriaBuilder.like(root.get("name"), group.getName()));
            transaction = session.getTransaction();
            transaction.begin();
            session.createQuery(criteriaDelete).executeUpdate();
            transaction.commit();
        } catch (Exception ex) {
            transactionRollback(transaction);
            logger.error("Delete group error");
            throw ex;
        } finally {
            sessionClose(session);
        }
    }

    @Override
    public void deleteAll() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaDelete<Group> criteriaDelete = criteriaBuilder.createCriteriaDelete(Group.class);
            criteriaDelete.from(Group.class);
            transaction = session.getTransaction();
            transaction.begin();
            session.createQuery(criteriaDelete).executeUpdate();
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
}

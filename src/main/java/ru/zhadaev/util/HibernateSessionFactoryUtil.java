package ru.zhadaev.util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Group.class);
                configuration.addAnnotatedClass(Course.class);
                configuration.addAnnotatedClass(Student.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (HibernateException ex) {

            }
        }

        return sessionFactory;
    }
}

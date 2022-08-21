package ru.zhadaev.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SpringConfig {
    private final String DRIVER_CLASS = "hibernate.connection.driver_class";
    private final String URL = "hibernate.connection.url";
    private final String USERNAME = "hibernate.connection.username";
    private final String PASSWORD = "hibernate.connection.password";

    @Bean
    public SessionFactory createSessionFactory() {
        Map<String, Object> settings = new HashMap<>();
        settings.put(DRIVER_CLASS, new HibernateProperties().getDriverClass());
        settings.put(URL, new HibernateProperties().getUrl());
        settings.put(USERNAME, new HibernateProperties().getUsername());
        settings.put(PASSWORD, new HibernateProperties().getPassword());

        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        MetadataSources metadataSources = new MetadataSources(standardServiceRegistry);
        metadataSources.addAnnotatedClass(Group.class);
        metadataSources.addAnnotatedClass(Course.class);
        metadataSources.addAnnotatedClass(Student.class);

        Metadata metadata = metadataSources.buildMetadata();

        SessionFactory sessionFactory = metadata.buildSessionFactory();

        return sessionFactory;
    }
}

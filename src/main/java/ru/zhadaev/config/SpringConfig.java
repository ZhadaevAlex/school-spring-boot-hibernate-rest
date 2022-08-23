package ru.zhadaev.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SpringConfig {
    private static final String DRIVER_CLASS = "hibernate.connection.driver_class";
    private static final String URL = "hibernate.connection.url";
    private static final String USERNAME = "hibernate.connection.username";
    private static final String PASSWORD = "hibernate.connection.password";

    private final HibernateProperties hibernateProperties;

    @Autowired
    public SpringConfig(HibernateProperties hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }

    @Bean
    public SessionFactory createSessionFactory() {
        Map<String, Object> settings = new HashMap<>();
        settings.put(DRIVER_CLASS, hibernateProperties.getDriverClass());
        settings.put(URL, hibernateProperties.getUrl());
        settings.put(USERNAME, hibernateProperties.getUsername());
        settings.put(PASSWORD, hibernateProperties.getPassword());

        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        MetadataSources metadataSources = new MetadataSources(standardServiceRegistry);
        metadataSources.addAnnotatedClass(Group.class);
        metadataSources.addAnnotatedClass(Course.class);
        metadataSources.addAnnotatedClass(Student.class);

        Metadata metadata = metadataSources.getMetadataBuilder().build();

        return metadata.getSessionFactoryBuilder().build();
    }
}

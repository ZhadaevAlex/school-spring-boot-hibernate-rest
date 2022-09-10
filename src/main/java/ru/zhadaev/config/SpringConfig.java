package ru.zhadaev.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SpringConfig {
    private static final String DRIVER_CLASS = "hibernate.connection.driver_class";
    private static final String URL = "hibernate.connection.url";
    private static final String USERNAME = "hibernate.connection.username";
    private static final String PASSWORD = "hibernate.connection.password";
    private static final String DATASOURCE_DRIVER_CLASS = "spring.datasource.driver-class-name";
    private static final String DATASOURCE_URL = "spring.datasource.url";
    private static final String DATASOURCE_USERNAME = "spring.datasource.username";
    private static final String DATASOURCE_PASSWORD = "spring.datasource.password";

    private final HibernateProperties hibernateProperties;
    private final Environment environment;

    @Autowired
    public SpringConfig(HibernateProperties hibernateProperties, Environment environment) {
        this.hibernateProperties = hibernateProperties;
        this.environment = environment;
    }

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty(DATASOURCE_DRIVER_CLASS));
        dataSource.setUrl(environment.getProperty(DATASOURCE_URL));
        dataSource.setUsername(environment.getProperty(DATASOURCE_USERNAME));
        dataSource.setPassword(environment.getProperty(DATASOURCE_PASSWORD));
        return dataSource;
    }

//    @Bean(name = "sessionFactory")
//    public SessionFactory createSessionFactory() throws IOException {
//        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
//        factoryBean.setDataSource(getDataSource());
//        factoryBean.setAnnotatedClasses(Group.class);
//        factoryBean.afterPropertiesSet();
//        SessionFactory sessionFactory = factoryBean.getObject();
//        return sessionFactory;
//    }

    @Bean(name = "sessionFactory")
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

//    @Bean
//    public HibernateTransactionManager getTransactionManager() throws IOException {
//        return new HibernateTransactionManager(createSessionFactory());
//    }
}

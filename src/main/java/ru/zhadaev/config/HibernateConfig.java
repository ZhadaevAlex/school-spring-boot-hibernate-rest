package ru.zhadaev.config;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionManager;
import ru.zhadaev.config.properties.HibernateProperties;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class HibernateConfig {
    private final HibernateProperties hibernateProperties;

    @Bean
    public DataSource dataSource() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(hibernateProperties.getUrl(), true);
        dataSource.setDriverClassName(hibernateProperties.getDriverClass());
        dataSource.setUsername(hibernateProperties.getUsername());
        dataSource.setPassword(hibernateProperties.getPassword());
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("ru.zhadaev.dao.entities");
        sessionFactory.setHibernateProperties(hibernateProperties.getHibernateProperties());
        return sessionFactory;
    }

    @Bean
    public TransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }
}

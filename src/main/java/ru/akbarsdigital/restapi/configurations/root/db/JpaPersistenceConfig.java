package ru.akbarsdigital.restapi.configurations.root.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Import(DataSourceConfig.class)
@EnableTransactionManagement//<!-- activate @Transactional JPA annotation --> <tx:annotation-driven/>
@EnableJpaRepositories(basePackages = {"ru.akbarsdigital.restapi.repository"}) // Enable and scan Spring Data repositories.
public class JpaPersistenceConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Environment environment;


    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String HIBERNATE_GENERATE_STATISTICS = "hibernate.generate_statistics";
    private static final String HIBERNATE_SESSION_FACTORY_NAME = "hibernate.session_factory_name";
    private static final String BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String ORDER_INSERTS = "hibernate.order_inserts";
    private static final String ORDER_UPDATES = "hibernate.order_updates";


    /**
     * <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean" >
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("ru.akbarsdigital.restapi.entity");
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactory.setJpaPropertyMap(mapAdditionalProperties());
        return entityManagerFactory;
    }

    private Map<String, ?> mapAdditionalProperties() {

        Map<String, String> properties = new HashMap<>();
        properties.put(HIBERNATE_HBM2DDL_AUTO, environment.getRequiredProperty(HIBERNATE_HBM2DDL_AUTO));
        properties.put(HIBERNATE_DIALECT, environment.getRequiredProperty(HIBERNATE_DIALECT));
        properties.put(HIBERNATE_SHOW_SQL, environment.getRequiredProperty(HIBERNATE_SHOW_SQL));
        properties.put(HIBERNATE_FORMAT_SQL, environment.getRequiredProperty(HIBERNATE_FORMAT_SQL));
        properties.put(HIBERNATE_SESSION_FACTORY_NAME, environment.getRequiredProperty(HIBERNATE_SESSION_FACTORY_NAME));
        properties.put(HIBERNATE_GENERATE_STATISTICS, environment.getRequiredProperty(HIBERNATE_GENERATE_STATISTICS));
        properties.put(BATCH_SIZE, environment.getRequiredProperty(BATCH_SIZE));
        properties.put(ORDER_INSERTS, environment.getRequiredProperty(ORDER_INSERTS));
        properties.put(ORDER_UPDATES, environment.getRequiredProperty(ORDER_UPDATES));
        return properties;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    //Automatic Transaction Participation
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }


    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}

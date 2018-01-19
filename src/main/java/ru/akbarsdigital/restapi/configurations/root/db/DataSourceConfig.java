package ru.akbarsdigital.restapi.configurations.root.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@Log4j2
public class DataSourceConfig {

    @Autowired
    private Environment environment;
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_NAME = "db.databaseName";
    private static final String DB_USER = "db.username";
    private static final String DB_PORT = "db.port";
    private static final String DB_HOST = "db.host";


    /**
     * <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
     */

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        HikariDataSource proxyDataSource = new HikariDataSource();
        proxyDataSource.setDataSource(this.getRealDataSource());
        proxyDataSource.setUsername(environment.getRequiredProperty(DB_USER));
        proxyDataSource.setPassword(environment.getRequiredProperty(DB_PASSWORD));
        proxyDataSource.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2 + 2);
        return proxyDataSource;
    }

    private DataSource getRealDataSource() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setDatabaseName(environment.getRequiredProperty(DB_NAME));
        pgSimpleDataSource.setPortNumber(environment.getRequiredProperty(DB_PORT, Integer.class));
        pgSimpleDataSource.setServerName(environment.getRequiredProperty(DB_HOST));
        return pgSimpleDataSource;
    }
}

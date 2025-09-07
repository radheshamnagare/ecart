package com.radhesham.ecart.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ECartDBConf {

    @Value("${spring.datasource.driver-class-name}")
    String dbDriver;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.url}")
    String dbUrl;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    int maxPoolSize;
    @Value("${spring.datasource.hikari.minimum-idle}")
    int minimumIdle;
    @Value("${spring.datasource.hikari.auto-commit}")
    boolean autoCommit;
    @Value("${spring.datasource.hikari.transaction-isolation}")
    String transactionIsolation;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(dbDriver);
        //configuration
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setAutoCommit(autoCommit);
        config.setTransactionIsolation(transactionIsolation);
        return new HikariDataSource(config);
    }



}

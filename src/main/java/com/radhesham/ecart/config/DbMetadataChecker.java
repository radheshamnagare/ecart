package com.radhesham.ecart.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Component
public class DbMetadataChecker {

    private static final Logger logger = LoggerFactory.getLogger(DbMetadataChecker.class);
    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void printDatabaseMetaData() {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            logger.info("Database driver: {}", metaData.getDriverName());
            logger.info("Database version: {}", metaData.getDatabaseProductVersion());
            logger.info("Autocommit mode: {}", conn.getAutoCommit());
            logger.info("Isolation level: {}", conn.getTransactionIsolation());
            if (dataSource instanceof HikariDataSource hikari) {
                logger.info("Minimum pool size: {}", hikari.getMinimumIdle());
                logger.info("Maximum pool size:{} ", hikari.getMaximumPoolSize());
            }
        } catch (Exception e) {
            logger.error("Exception in printDatabaseMetaData:", e);
        }
    }
}

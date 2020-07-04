package com.easy.ls;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * springBoot多数据源配置
 */
@Configuration
public class DataSourceConfig {
    /**
     * 主数据源
     * @return
     */
    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.auto")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * 第二数据源
     * @return
     */
    @Bean(name = "secondaryDataSource")
    @Qualifier("secondaryDataSource")
    @ConfigurationProperties(prefix="spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     *  事务管理器
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("primaryDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}

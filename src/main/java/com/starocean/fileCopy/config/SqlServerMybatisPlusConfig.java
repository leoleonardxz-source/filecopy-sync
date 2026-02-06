package com.starocean.fileCopy.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

@Configuration
@MapperScan(
    basePackages = "com.starocean.fileCopy.db.sqlserver.mapper",
    sqlSessionTemplateRef = "sqlserverSqlSessionTemplate"
)
public class SqlServerMybatisPlusConfig {

    @Bean(name = "sqlserverDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    DataSource sqlserverDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlserverSqlSessionFactory")
    SqlSessionFactory sqlserverSqlSessionFactory(@Qualifier("sqlserverDataSource") DataSource ds) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(ds);
        return factoryBean.getObject();
    }

    @Bean(name = "sqlserverSqlSessionTemplate")
    SqlSessionTemplate sqlserverSqlSessionTemplate(@Qualifier("sqlserverSqlSessionFactory") SqlSessionFactory sf) {
        return new SqlSessionTemplate(sf);
    }

    @Bean(name = "sqlserverTxManager")
    DataSourceTransactionManager sqlserverTxManager(@Qualifier("sqlserverDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}


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
    basePackages = "com.starocean.fileCopy.db.mysql.mapper",
    sqlSessionTemplateRef = "mysqlSqlSessionTemplate"
)
public class MysqlMybatisPlusConfig {

    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlSqlSessionFactory")
    SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource ds) throws Exception {
        // MyBatis-Plus 用 MybatisSqlSessionFactoryBean
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(ds);
        return factoryBean.getObject();
    }

    @Bean(name = "mysqlSqlSessionTemplate")
    SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sf) {
        return new SqlSessionTemplate(sf);
    }

    @Bean(name = "mysqlTxManager")
    DataSourceTransactionManager mysqlTxManager(@Qualifier("mysqlDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}


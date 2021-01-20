package com.seckill.demo.demo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.seckill.demo.demo.config.datasource.DynamicDataSource;

/**
 * 数据源配置
 *<p>Description: </p>
 * @ClassName: DataSourceConfiguration
 * @author wangwenzhao
 * @date Jan 20, 20213:32:19 PM
 * @version: V1.0
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DynamicDataSource.MASTER_DATA_SOURCE, masterDataSource);
        targetDataSources.put(DynamicDataSource.SLAVE_DATA_SOURCE, slaveDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(masterDataSource);

        return dataSource;
    }

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "dynamicSqlSessionFactory")
    public SqlSessionFactory dynamicSqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml");
        Resource[] anotherResources = new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml");

        bean.setMapperLocations((Resource[]) ArrayUtils.addAll(resources, anotherResources));
        return bean.getObject();
    }

    @Bean(name = "dynamicTransactionManager")
    public PlatformTransactionManager dynamicTransactionManager(DynamicDataSource dataSource) {
        logger.info("dynamic datasource transaction manager is init");
        return new DataSourceTransactionManager(dataSource);
    }
}

/*
 * Copyright (c) 2017 All Rights Reserved, Jinxiudadi
 *
 * @Title: DataSourceAspect.java
 */

package com.seckill.demo.demo.config.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Yan Jingchao
 * @ClassName: DataSourceAspect
 * @date: 2017年03月10日
 * @version: V1.0
 */

@Component
@Aspect
@Order(-1)
public class DataSourceAspect {

    private final static String[] slaveDataSourceMethods = {"FindService", "GetService", "ListService"};


    @Value("${spring.datasource.multiDataSource: false}")
    private String isEnableMultiDataSource;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.jxdd.*.service.*.*(..))")
    public void dataSourceHolder(JoinPoint point) {
        if (!Boolean.parseBoolean(isEnableMultiDataSource)) {
            return;
        }
        String methodName = point.getSignature().getName();
        logger.info("execute method:" + methodName + " changing datasource...");
        if (chooseDataSource(methodName)) {
            logger.info("change datasource to slave datasource");
            DynamicDataSource.setDatabaseType(DynamicDataSource.SLAVE_DATA_SOURCE);
        } else {
            logger.info("change datasource to master datasource");
        }
    }


    private Boolean chooseDataSource(String methodName) {
        for (String slaveDataSourceMethod : slaveDataSourceMethods) {
            if (methodName.endsWith(slaveDataSourceMethod)) {
                return true;
            }
        }
        return false;
    }
}

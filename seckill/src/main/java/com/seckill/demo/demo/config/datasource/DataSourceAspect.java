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
 *<p>Description: </p>
 * @ClassName: DataSourceAspect
 * @author wangwenzhao
 * @date Jan 20, 20213:33:16 PM
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

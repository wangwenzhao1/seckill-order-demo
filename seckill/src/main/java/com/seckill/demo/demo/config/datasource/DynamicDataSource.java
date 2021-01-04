/*
 * Copyright (c) 2017 All Rights Reserved, Jinxiudadi
 *
 * @Title: DatabaseContextHolder.java
 */

package com.seckill.demo.demo.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Yan Jingchao
 * @ClassName: DynamicDataSource
 * @date: 2017年03月10日
 * @version: V1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public static final String MASTER_DATA_SOURCE = "masterDataSource";

    public static final String SLAVE_DATA_SOURCE = "slaveDataSource";

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDatabaseType(String type) {
        contextHolder.set(type);
    }

    public static String getDatabaseType() {
        return contextHolder.get();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDatabaseType();
    }
}

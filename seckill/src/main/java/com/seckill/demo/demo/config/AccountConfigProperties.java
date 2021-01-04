package com.seckill.demo.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class AccountConfigProperties {
    @Value("${redis.pool.host}")
    private String redisPoolHost;
    @Value("${redis.pool.port}")
    private String redisPoolPort;
    @Value("${redis.pool.maxIdle}")
    private String redisPoolMaxIdle;
    @Value("${redis.pool.maxTotal}")
    private String redisPoolMaxTotal;
    @Value("${redis.pool.timeBetweenEvictionRunsMillis}")
    private String redisPoolTimeBetweenEvictionRunsMillis;
    @Value("${redis.pool.minEvictableIdleTimeMillis}")
    private String redisPoolMinEvictableIdleTimeMillis;
    @Value("${redis.timeout}")
    String redisTimeout;
    @Value("${redis.web.database}")
    String redisWebDatabase;

    public String getRedisPoolHost() {
        return redisPoolHost;
    }

    public void setRedisPoolHost(String redisPoolHost) {
        this.redisPoolHost = redisPoolHost;
    }

    public String getRedisPoolPort() {
        return redisPoolPort;
    }

    public void setRedisPoolPort(String redisPoolPort) {
        this.redisPoolPort = redisPoolPort;
    }

    public String getRedisPoolMaxIdle() {
        return redisPoolMaxIdle;
    }

    public void setRedisPoolMaxIdle(String redisPoolMaxIdle) {
        this.redisPoolMaxIdle = redisPoolMaxIdle;
    }

    public String getRedisPoolMaxTotal() {
        return redisPoolMaxTotal;
    }

    public void setRedisPoolMaxTotal(String redisPoolMaxTotal) {
        this.redisPoolMaxTotal = redisPoolMaxTotal;
    }

    public String getRedisPoolTimeBetweenEvictionRunsMillis() {
        return redisPoolTimeBetweenEvictionRunsMillis;
    }

    public void setRedisPoolTimeBetweenEvictionRunsMillis(String redisPoolTimeBetweenEvictionRunsMillis) {
        this.redisPoolTimeBetweenEvictionRunsMillis = redisPoolTimeBetweenEvictionRunsMillis;
    }

    public String getRedisPoolMinEvictableIdleTimeMillis() {
        return redisPoolMinEvictableIdleTimeMillis;
    }

    public void setRedisPoolMinEvictableIdleTimeMillis(String redisPoolMinEvictableIdleTimeMillis) {
        this.redisPoolMinEvictableIdleTimeMillis = redisPoolMinEvictableIdleTimeMillis;
    }

    public String getRedisTimeout() {
        return redisTimeout;
    }

    public void setRedisTimeout(String redisTimeout) {
        this.redisTimeout = redisTimeout;
    }

    public String getRedisWebDatabase() {
        return redisWebDatabase;
    }

    public void setRedisWebDatabase(String redisWebDatabase) {
        this.redisWebDatabase = redisWebDatabase;
    }

    public Map<String, String> getProperty() {
        Map<String, String> ps = new HashMap<String, String>();
        ps.put("redis.pool.host", this.getRedisPoolHost());
        ps.put("redis.pool.port", this.getRedisPoolPort());
        ps.put("redis.pool.maxIdle", this.getRedisPoolMaxIdle());
        ps.put("redis.pool.maxTotal", this.getRedisPoolMaxTotal());
        ps.put("redis.timeout", this.getRedisTimeout());
        ps.put("redis.web.database", this.getRedisWebDatabase());
        // ps.put("sms.url", this.getSmsUrl());
        return ps;
    }
}

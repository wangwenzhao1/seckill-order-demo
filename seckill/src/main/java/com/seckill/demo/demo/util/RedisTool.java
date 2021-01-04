package com.seckill.demo.demo.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seckill.demo.demo.config.ConfigHelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@Component
public class RedisTool {

    public RedisTool() {
        super();
    }

    public RedisTool(ConfigHelper cfh, Logger logger, int expire, String password, JedisPool jedisPool,
            String redisPoolHost, String redisPoolPort, String redisTimeout, String redisWebDatabase) {
        super();
        this.cfh = cfh;
        this.logger = logger;
        this.expire = expire;
        this.password = password;
        this.jedisPool = jedisPool;
        this.redisPoolHost = redisPoolHost;
        this.redisPoolPort = redisPoolPort;
        this.redisTimeout = redisTimeout;
        this.redisWebDatabase = redisWebDatabase;
    }

    @Autowired
    private ConfigHelper cfh;

    private Logger logger = LoggerFactory.getLogger(RedisTool.class);
    private int expire = Integer.parseInt(System.getProperty("redis.expire", "604800000"));

    private String password = null;

    private JedisPool jedisPool;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init(){
        expire = Integer.parseInt(System.getProperty("redis.expire", "604800000"));
        if(jedisPool == null){
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            // poolConfig.setMaxActive((int)cfh.getConfig("redis.maxActive",
            // 100));
            poolConfig.setMaxIdle((int)cfh.getConfig("redis.maxIdle", 50));
            poolConfig.setMaxWaitMillis(cfh.getConfig("redis.maxWait",
                    15000));
            poolConfig.setMinEvictableIdleTimeMillis(cfh.getConfig("redis.pool.minEvictableIdleTimeMillis", 222));
            poolConfig.setNumTestsPerEvictionRun(3);
            poolConfig.setTimeBetweenEvictionRunsMillis(cfh.getConfig("redis.pool.timeBetweenEvictionRunsMillis", 80000));
            // poolConfig.setWhenExhaustedAction((byte)
            // cfh.getConfig("redis.whenExhaustedAction", 1));

            String host = redisPoolHost == null ? cfh.getConfig("redis.pool.host","****") : redisPoolHost;
            int port = redisPoolPort == null ? 6379 : Integer.parseInt(redisPoolPort);
            int timeout = redisTimeout == null ? 15000 : Integer.parseInt(redisTimeout);
            int dbId = redisWebDatabase == null ? 2 : Integer.parseInt(redisWebDatabase);

            logger.debug("redis connect to " + host);

            jedisPool = new JedisPool(poolConfig, host, port, timeout, password, dbId);
        }
    }

    @Value("${redis.pool.host}")
    String redisPoolHost;

    @Value("${redis.pool.port}")
    String redisPoolPort;

    @Value("${redis.timeout}")
    String redisTimeout;

    @Value("${redis.web.database}")
    String redisWebDatabase;

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public String get(String key) {
        String value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        if(value == null) {
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String set(String key, String value, int expire) {
        if(value == null) {
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return value;
    }

    public String hget(final String key, final String field) {
        String value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.hget(key, field);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return value;
    }

    public void hdel(final String key, final String field) {
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.hdel(key, field);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
    }

    public String hset(final String key, final String field, final String value) {
        if(value == null) {
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.hset(key, field, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return value;
    }

    public List<String> lrange(final String key, int start, int stop) {
        Jedis jedis = jedisPool.getResource();
        List<String> result = null;

        try {
            result = jedis.lrange(key, start, stop);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }

        return result;
    }

    public void lpush(final String key, final String value) {
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.lpush(key, value);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
    }

    public boolean exists(final String key) {
        Jedis jedis = jedisPool.getResource();
        boolean result = false;

        try {
            result = jedis.exists(key);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }

        return result;
    }

    public boolean hexists(final String key, final String field_name) {
        Jedis jedis = jedisPool.getResource();
        boolean result = false;

        try {
            result = jedis.hexists(key, field_name);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }

        return result;
    }

    /**
     * del
     *
     * @param key
     */
    public void del(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
    }

    /**
     * flush
     */
    public void flushDB() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.flushDB();
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
    }

    /**
     * size
     */
    public Long dbSize() {
        Long dbSize = 0L;
        Jedis jedis = jedisPool.getResource();
        try {
            dbSize = jedis.dbSize();
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return dbSize;
    }


    public int getExpire() {
        return expire;
    }

    /**
     * @author wangwenzhao
     * @param string
     */
    public Long incr(String string) {
        Jedis jedis = jedisPool.getResource();
        Long id = 0L;
        try {
            id = jedis.incr(string);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return id;


    }
    public Long decr(String string) {
        Jedis jedis = jedisPool.getResource();
        Long id = 0L;
        try {
            id = jedis.decr(string);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return id;


    }

    /**
     * @author wangwenzhao
     * @return
     */
    public List<String> blpop(String key) {
        Jedis jedis = jedisPool.getResource();
        List<String> list = new ArrayList<>();
        try {
            list = jedis.blpop(1000, key);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return list;

    }

    /**
     * @author wangwenzhao
     * @param string
     * @return
     */
    public List<String> brpop(String string) {
        Jedis jedis = jedisPool.getResource();
        List<String> list = null;
        try {
            list = jedis.brpop(1000, string);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return list;

    }

    /**
     * @author wangwenzhao
     * @return
     */
    public Long sadd(String key , String ... member ) {

        Jedis jedis = jedisPool.getResource();
        Long id = 0L;
        try {
            id = jedis.sadd(key, member);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return id;

    }

    /**
     * @author wangwenzhao
     * @param string
     * @return
     */
    public Set<String> sem(String string) {
        Jedis jedis = jedisPool.getResource();
        Set<String> id = new HashSet<>();
        try {
            id = jedis.smembers(string);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return id;

    }

    /**
     * @author wangwenzhao
     * @param string
     * @return
     */
    public Long zset(String string,double score, String vString) {
        Jedis jedis = jedisPool.getResource();
        Long id = 0L;
        try {
            id = jedis.zadd(string, score, vString);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return id;


    }
    /**
     * @author wangwenzhao
     * @param string
     * @return
     */
    public Set<String> zrange(String key,long start, long end) {
        Jedis jedis = jedisPool.getResource();
        Set<String> id = new HashSet<>();
        try {
            id = jedis.zrange(key, start, end);
        } catch (Exception ex) {
            logger.error("redis", ex);
            throw ex;
        } finally {
            jedis.close();
        }
        return id;


    }


}

package com.neuedu.busines.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class JedisConfig {

    @Value("${redis.maxTotal}")
    private Integer maxTotal;
    @Value("${redis.maxIdle}")
    private Integer maxIdle;
    @Value("${redis.minIdle}")
    private Integer minIdle;
    @Value("${redis.blockWhenExhausted}")
    private boolean blockWhenExhausted;
    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.testOnReturn}")
    private boolean testOnReturn;
    @Value("${redis.jmxEnabled}")
    private boolean jmxEnabled;
    @Value("${redis.redisHost}")
    private String redisHost;
    @Value("${redis.redisPort}")
    private Integer redisPort;
    @Value("@{redis.redisPassword}")
    private String redisPassword;
    @Value("${redis.timeout}")
    private Integer timeout;

    @Bean
    public GenericObjectPoolConfig setPoolConfig() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setBlockWhenExhausted(blockWhenExhausted);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setJmxEnabled(jmxEnabled);
        return poolConfig;
    }

    /**
     * 初始化jedispool,交给IOC管理
     */
    @Bean
    public JedisPool jedisPool() {
        JedisPool jedisPool = new JedisPool(setPoolConfig(), redisHost, redisPort, timeout);
        return jedisPool;
    }
    /*@Bean
    public JedisSentinelPool sentinelPool(){
        Set<String> set = new HashSet<>();
        //设置三台哨兵的ip节点
        set.add("");
        set.add("");
        set.add("");
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(
                "mymaster",
                set,
                setPoolConfig(),
                timeout,
                redisPassword);
        return jedisSentinelPool;
    }*/

}

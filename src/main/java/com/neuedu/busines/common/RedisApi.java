package com.neuedu.busines.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 封装REDISapi
 */
@Component
public class RedisApi {
    @Autowired
    JedisPool jedisPool;

    /**
     * 添加字符串
     * @param key
     * @param value
     * @return
     */
    public String set(String key,String value){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    /**
     * 获取value
     * @param key
     * @return
     */
    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    /**
     * key存在设置失败，不存在设置成功
     * @param key
     * @param value
     * @return
     */
    public Long setnx(String key,String value){
        Jedis jedis = jedisPool.getResource();
        Long setnx = jedis.setnx(key, value);
        jedis.close();
        return setnx;
    }

    /**
     * 原子性
     * @param key
     * @param value
     * @return
     */
    public String getset(String key,String value){
        Jedis jedis = jedisPool.getResource();
        String getSet = jedis.getSet(key, value);
        jedis.close();
        return getSet;
    }

    /**
     * 设置过期时间
     * @param key
     * @param time
     * @return
     */
    public Long expire(String key,int time){
        Jedis jedis = jedisPool.getResource();
        Long expire = jedis.expire(key, time);
        jedis.close();
        return expire;
    }

    /**
     * 查看剩余时间
     */
    public Long ttl(String key){
        Jedis jedis = jedisPool.getResource();
        Long ttl = jedis.ttl(key);
        jedis.close();
        return ttl;
    }

    /**
     * 创建key value同时设置过期时间
     * @param key
     * @param time
     * @param value
     * @return
     */
    public String setEx(String key,int time,String value){
        Jedis jedis = jedisPool.getResource();
        String setex = jedis.setex(key, time, value);
        jedis.close();
        return setex;
    }

}

package com.neuedu.busines.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 封装REDISapi
 */
@Component
public class RedisApi {
    @Autowired
    JedisPool jedisPool;

    /**
     * 添加字符串
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    /**
     * 获取value
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    /**
     * key存在设置失败，不存在设置成功
     *
     * @param key
     * @param value
     * @return
     */
    public Long setnx(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        Long setnx = jedis.setnx(key, value);
        jedis.close();
        return setnx;
    }
    public Long remove(String key){
        Jedis jedis = jedisPool.getResource();
        Long del = jedis.del(key);
        jedis.close();
        return del;
    }


    /**
     * 原子性
     *
     * @param key
     * @param value
     * @return
     */
    public String getset(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String getSet = jedis.getSet(key, value);
        jedis.close();
        return getSet;
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public Long expire(String key, int time) {
        Jedis jedis = jedisPool.getResource();
        Long expire = jedis.expire(key, time);
        jedis.close();
        return expire;
    }

    /**
     * 查看剩余时间
     */
    public Long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        Long ttl = jedis.ttl(key);
        jedis.close();
        return ttl;
    }

    /**
     * 创建key value同时设置过期时间
     *
     * @param key
     * @param time
     * @param value
     * @return
     */
    public String setEx(String key, int time, String value) {
        Jedis jedis = jedisPool.getResource();
        String setex = jedis.setex(key, time, value);
        jedis.close();
        return setex;
    }

    /**
     * 哈希结构-api封装
     * 设置key，field,value
     */

    public Long hset(String key, String field, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(key, field, value);
        jedis.close();
        return result;

    }

    /**
     * 哈希结构-api封装
     * 批量设置key，field,value
     */

    public String hset(String key, Map<String, String> map) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hmset(key, map);
        jedis.close();
        return result;

    }

    /**
     * 哈希结构-api封装
     * 根据key，field查看value
     */

    public String hget(String key, String feild) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(key, feild);
        jedis.close();
        return result;

    }

    /**
     * 哈希结构-api封装
     * 根据key，查看所有的field、value
     */

    public Map<String, String> hgetAll(String key) {
        Jedis jedis = jedisPool.getResource();
        Map<String, String> result = jedis.hgetAll(key);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 根据key，查看所有的feild
     */

    public Set<String> hgetAllField(String key) {
        Jedis jedis = jedisPool.getResource();
        Set<String> result = jedis.hkeys(key);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 根据key，查看所有的value
     */

    public List<String> hgetAllVals(String key) {
        Jedis jedis = jedisPool.getResource();
        List<String> result = jedis.hvals(key);
        jedis.close();
        return result;
    }


    /**
     * 哈希结构-api封装
     * 计数器
     */

    public Long hgetAllVals(String key, String field, Long incr) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hincrBy(key, field, incr);
        jedis.close();
        return result;
    }


    /**
     * 发布消息
     */

    public void pub(String channel, String message) {

        Jedis jedis = jedisPool.getResource();
        System.out.println("  >>> fabu(PUBLISH) > Channel:" + channel + " > fa chu de Message:" + message);
        jedis.publish(channel, message);
        jedis.close();
    }

    /**
     * 订阅消息
     */

    public void subscribe(JedisPubSub listener, String channel) {
        Jedis jedis = jedisPool.getResource();
        jedis.subscribe(listener, channel);
        jedis.close();
    }

    /**
     * 取消订阅消息
     */

    public void unsubscribe(JedisPubSub listener, String channel) {
        Jedis jedis = jedisPool.getResource();
        System.out.println("  >>> qu xiao ding yue(UNSUBSCRIBE) > Channel:" + channel);
        listener.unsubscribe(channel);
        jedis.close();
    }


}

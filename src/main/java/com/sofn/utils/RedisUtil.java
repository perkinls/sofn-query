package com.sofn.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    private static final String ip = "192.168.21.101";
    private static final int port = 6379;
    private static JedisPool jedisPool;

    public static Jedis getJedis() {
        if(null == jedisPool){
            initialPool();
        }
        return jedisPool.getResource();
    }

    private static void initialPool()
    {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
        jedisPool = new JedisPool(config,ip,port);
    }

    public static void main(String[] args){
        System.out.println(getJedis());
    }
}

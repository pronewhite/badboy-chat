package com.hitsz.badboyChat.common.user.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCommonProcessor {

    @Autowired
    private RedisTemplate redisTemplate;

    // 通过key获取value
    public Object get(String key){
        if(key == null){
            throw new UnsupportedOperationException("Redis key could not be null");
        }
        return redisTemplate.opsForValue().get(key);
    }

    // 向redis中存入key: value 数据对
    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }
    // 向redis中存入key：value数据对，并支持过期时间
    public void set(String key, Object value, Long time){
        if(time > 0){
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        }else{
            set(key, value);
        }
    }

    public void set(String key, Object value, Long time, TimeUnit unit){
        if(time > 0){
            redisTemplate.opsForValue().set(key, value, time, unit);
        }else{
            set(key, value);
        }
    }

    // 从redis中删除元素
    public void remove(String key){
        if(key == null){
            throw new UnsupportedOperationException(" key 不能为空");
        }
        redisTemplate.delete(key);
    }

    /**
     * 获取key的过期时间
     * @param key
     * @param unit
     * @return
     */
    public Long getExpire(String key, TimeUnit unit){
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 给key设置过期时间
     * @param key
     * @param time
     * @param unit
     */
    public void expire(String key, Long time, TimeUnit unit){
        redisTemplate.expire(key, time, unit);
    }
}

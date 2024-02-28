package com.hitsz.badboyChat.common.user.utils;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RedisCommonProcessor {

    @Autowired
    private static RedisTemplate redisTemplate;

    public static <T> void mset(Map<String,T> loadMap, Long expireSeconds) {
        redisTemplate.opsForValue().multiSet(loadMap);
        loadMap.forEach((key, value)-> {
            expire(key, expireSeconds);
        });
    }

    public static void del(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public static Boolean zAdd(String key, Long value, long time) {
        return redisTemplate.opsForZSet().add(key,value, time);
    }

    /**
     * 返回Redis中指定键（key）下的有序集合元素的数量。
     * @param key 指定key
     * @return
     */
    public static Long zcard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    public static Set<ZSetOperations.TypedTuple<String>> zReverseRangeWithScores(String redisKey, Integer pageSize) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(redisKey, Double.MIN_VALUE,
                Double.MAX_VALUE, 0, pageSize);
    }

    public static Set<ZSetOperations.TypedTuple<String>> zReverseRangeByScoreWithScores(String redisKey, double max, Integer pageSize) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(redisKey, Double.MIN_VALUE, max,
                1, pageSize);
    }


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
    public static void expire(String key, Long time, TimeUnit unit){
        redisTemplate.expire(key, time, unit);
    }

    /**
     * 给key设置过期时间
     * @param key
     * @param time
     */
    public static void expire(String key, Long time){
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    // 根据key批量获取值
    public static <T> List<T> mget(Collection<String> keys, Class<T> tClass){
        List<String> list = redisTemplate.opsForValue().multiGet(keys);
        if(Objects.isNull(list)){
            return new ArrayList<>();
        }
        return list.stream().map(o -> toBeanOrNull(o, tClass)).collect(Collectors.toList());
    }

    static <T> T toBeanOrNull(String o, Class<T> tClass) {
        return o == null ? null : JSONUtil.toBean(o, tClass);
    }

    public void zRemove(String offlineKey, Long uid) {
        redisTemplate.opsForZSet().remove(offlineKey, uid);
    }

    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param key
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key,
                                                                                 Double min, Double max) {
        if (Objects.isNull(min)) {
            min = Double.MIN_VALUE;
        }
        if (Objects.isNull(max)) {
            max = Double.MAX_VALUE;
        }
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }
}

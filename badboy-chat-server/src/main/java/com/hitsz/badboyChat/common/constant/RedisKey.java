package com.hitsz.badboyChat.common.constant;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/25 14:19
 */
public class RedisKey {

    public static final String BASE_KEY = "mallchat:";

    public static final String USER_REDIS_KEY = "userToken:uid_%d";

    public static String getKey(String key, Object... args) {
        return BASE_KEY + String.format(key, args);
    }
}

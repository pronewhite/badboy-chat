package com.hitsz.badboyChat.common.constant;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/25 14:19
 */
public class RedisKey {

    public static final String BASE_KEY = "mallchat:";

    public static final String USER_REDIS_KEY = "userToken:uid_%d";
    public static final String USRE_MODIFY_KEY = "userModify:uid_%d";
    public static final String USER_SUMMERY_KEY = "userSummery:uid_%d";
    public static final String USER_INFO_KEY = "userInfo:uid_%d";
    public static final String ROOM_INFO_KEY = "roomInfo:roomId_%d";

    public static String getKey(String key, Object... args) {
        return BASE_KEY + String.format(key, args);
    }
}

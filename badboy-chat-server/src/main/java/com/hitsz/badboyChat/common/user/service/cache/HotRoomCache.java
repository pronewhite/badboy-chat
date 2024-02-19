package com.hitsz.badboyChat.common.user.service.cache;

import com.hitsz.badboyChat.common.constant.RedisKey;
import com.hitsz.badboyChat.common.user.utils.RedisCommonProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 22:06
 */
@Component
public class HotRoomCache {
    public void refreshHotRoomActiveTime(Long roomId, Date createTime) {
        RedisCommonProcessor.zAdd(RedisKey.getKey(RedisKey.HOT_ROOM_ACTIVE_TIME_ZSET), roomId, createTime.getTime());
    }
}

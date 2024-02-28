package com.hitsz.badboyChat.common.user.service.cache;

import cn.hutool.core.lang.Pair;
import com.hitsz.badboyChat.common.constant.RedisKey;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.utils.CursorUtils;
import com.hitsz.badboyChat.common.user.utils.RedisCommonProcessor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

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

    public Set<ZSetOperations.TypedTuple<String>> getHotRooms(Double hotStart, Double hotEnd) {
        return RedisCommonProcessor.zRangeByScoreWithScores(RedisKey.getKey(RedisKey.HOT_ROOM_ACTIVE_TIME_ZSET), hotStart, hotEnd);
    }

    public CursorPageBaseResp<Pair<Long, Double>> getRoomCursorPage(CursorPageBaseReq req) {
        return CursorUtils.getCursorPageByRedis(req, RedisKey.getKey(RedisKey.HOT_ROOM_ACTIVE_TIME_ZSET) , Long::parseLong);
    }
}

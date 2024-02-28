package com.hitsz.badboyChat.common.user.service.cache;

import com.hitsz.badboyChat.common.constant.RedisKey;
import com.hitsz.badboyChat.common.service.cache.AbstractRedisStringCache;
import com.hitsz.badboyChat.common.user.dao.RoomFriendDao;
import com.hitsz.badboyChat.common.user.domain.entity.RoomFriend;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/27 15:15
 */
@Component
public class RoomFriendCache extends AbstractRedisStringCache<Long, RoomFriend> {
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Override
    protected String getKey(Long req) {
        return RedisKey.getKey(RedisKey.ROOM_FRIEND, req);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, RoomFriend> load(List<Long> req) {
        List<RoomFriend> roomFriends = roomFriendDao.listByIds(req);
        return roomFriends.stream().collect(Collectors.toMap(RoomFriend::getRoomId, Function.identity()));
    }
}

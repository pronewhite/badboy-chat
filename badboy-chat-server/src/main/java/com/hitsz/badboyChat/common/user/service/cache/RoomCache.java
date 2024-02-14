package com.hitsz.badboyChat.common.user.service.cache;

import com.hitsz.badboyChat.common.constant.RedisKey;
import com.hitsz.badboyChat.common.service.cache.AbstractRedisStringCache;
import com.hitsz.badboyChat.common.user.dao.RoomDao;
import com.hitsz.badboyChat.common.user.domain.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 12:45
 */
@Component
public class RoomCache extends AbstractRedisStringCache<Long, Room> {

    @Autowired
    private RoomDao roomDao;
    @Override
    protected String getKey(Long uid) {
        return RedisKey.getKey(RedisKey.ROOM_INFO_KEY, uid.toString());
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60 * 60L;
    }

    @Override
    protected Map<Long, Room> load(List<Long> roomList) {
        List<Room> rooms = roomDao.listByIds(roomList);
        return rooms.stream().collect(Collectors.toMap(Room::getId, Function.identity()));
    }
}

package com.hitsz.badboyChat.common.user.service.cache;

import com.hitsz.badboyChat.common.chat.service.dao.GroupMemBerDao;
import com.hitsz.badboyChat.common.chat.service.dao.RoomGroupDao;
import com.hitsz.badboyChat.common.user.domain.entity.RoomGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 22:33
 */
@Component
public class RoomGroupCache {

    @Autowired
    private RoomGroupDao roomGroupDao;
    @Autowired
    private GroupMemBerDao groupMemBerDao;

    @Cacheable(value = "roomGroupMembers", key = "'group' + #roomId")
    public List<Long> getRoomGrouopMembersUid(Long roomId) {
        RoomGroup roomGroup = roomGroupDao.getRoomGroupByRoomId(roomId);
        if(Objects.isNull(roomGroup)){
            return new ArrayList<>();
        }
        return groupMemBerDao.getGroupMembersUid(roomGroup.getId());
    }

    @Cacheable(value = "roomGroup", key = "'group:' + #roomId")
    public RoomGroup getGroupByRoomId(Long roomId) {
        return roomGroupDao.getRoomGroupByRoomId(roomId);
    }
}

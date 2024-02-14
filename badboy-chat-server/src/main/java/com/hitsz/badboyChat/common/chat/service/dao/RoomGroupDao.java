package com.hitsz.badboyChat.common.chat.service.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.RoomGroup;
import com.hitsz.badboyChat.common.user.mapper.RoomGroupMapper;
import org.springframework.stereotype.Service;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 13:21
 */
@Service
public class RoomGroupDao extends ServiceImpl<RoomGroupMapper, RoomGroup>{
    public RoomGroup getRoomGroupById(Long roomId) {
        return lambdaQuery()
                .eq(RoomGroup::getRoomId, roomId)
                .eq(RoomGroup::getIsDeleted, Boolean.FALSE)
                .one();
    }
}

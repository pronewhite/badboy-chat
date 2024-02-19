package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.enums.RoomTypeEnum;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.Room;
import com.hitsz.badboyChat.common.user.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/8 18:34
 */
@Service
public class RoomDao extends ServiceImpl<RoomMapper, Room>{


    public void refreshRoomMessage(Long roomId, Long msgId, Date createTime) {
        lambdaUpdate()
                .eq(Room::getId, roomId)
                .set(Room::getLastMsgId, msgId)
                .set(Room::getActiveTime, createTime)
                .update();
    }
}

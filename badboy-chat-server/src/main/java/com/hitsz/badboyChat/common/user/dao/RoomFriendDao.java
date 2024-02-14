package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.enums.RoomStatusEnum;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.RoomFriend;
import com.hitsz.badboyChat.common.user.mapper.RoomFriendMapper;
import org.springframework.stereotype.Service;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/8 18:34
 */
@Service
public class RoomFriendDao extends ServiceImpl<RoomFriendMapper, RoomFriend>{


    public RoomFriend getRoomByRoomKey(String roomKey) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomKey, roomKey)
                .one();
    }

    public void updateRoomFriendStatus(RoomFriend roomFriend) {
        lambdaUpdate()
                .eq(RoomFriend::getId, roomFriend.getRoomId())
                .set(RoomFriend::getStatus, YesOrNoEnum.NO.getCode())
                .update();
    }

    public void disableRoom(String roomKey) {
        lambdaUpdate()
                .eq(RoomFriend::getRoomKey, roomKey)
                .set(RoomFriend::getStatus, RoomStatusEnum.ABNORMAL.getCode())
                .update();
    }

    public RoomFriend getByRoomId(Long roomId) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomId, roomId)
                .eq(RoomFriend::getStatus, YesOrNoEnum.NO.getCode())
                .eq(RoomFriend::getIsDeleted, Boolean.FALSE)
                .one();
    }
}


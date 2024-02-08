package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.dao.RoomDao;
import com.hitsz.badboyChat.common.user.dao.RoomFriendDao;
import com.hitsz.badboyChat.common.user.domain.entity.Room;
import com.hitsz.badboyChat.common.user.domain.entity.RoomFriend;
import com.hitsz.badboyChat.common.user.mapper.RoomMapper;
import com.hitsz.badboyChat.common.user.service.RoomService;
import com.hitsz.badboyChat.common.user.service.adapter.FriendAdapter;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
* @author lenovo
* @description 针对表【room(房间表)】的数据库操作Service实现
* @createDate 2024-02-07 13:26:12
*/
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private RoomDao roomDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomFriend createRoomFriend(List<Long> roomFriendsUids) {
        AssertUtil.isNotEmpty(roomFriendsUids,"房间好友不能为空");
        AssertUtil.equal(roomFriendsUids.size(),2,"房间好友必须为2个");
        String roomKey = FriendAdapter.generateRoomKey(roomFriendsUids);
        RoomFriend roomFriend = roomFriendDao.getRoomByRoomKey(roomKey);
        // 如果roomFriend 不为空，那么表示双方之前为好友，现在是恢复好友关系，这种情况需要恢复房间的状态
        if(Objects.nonNull(roomFriend)){
            roomFriendDao.updateRoomFriendStatus(roomFriend);
        }else{
            // 否则表示这是双方第一次添加好友，需要创建房间
            Room room = createRoom();
            roomFriend = createFriendRoom(room.getId(), roomFriendsUids,roomKey);
        }
        return roomFriend;
    }

    @Override
    public void disableRoom(List<Long> uids) {
        AssertUtil.isNotEmpty(uids,"用户id不能为空");
        AssertUtil.equal(uids.size(),2,"房间用户必须为两个");
        String roomKey = FriendAdapter.generateRoomKey(uids);
        roomFriendDao.disableRoom(roomKey);
    }

    private RoomFriend createFriendRoom(Long id, List<Long> roomFriendsUids, String roomKey) {
        RoomFriend roomFriend = FriendAdapter.buildRoomFriend(id, roomFriendsUids,roomKey);
        roomFriendDao.save(roomFriend);
        return roomFriend;
    }

    private Room createRoom() {
        Room room = FriendAdapter.buildInserRoom();
        roomDao.save(room);
        return room;
    }
}





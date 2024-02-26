package com.hitsz.badboyChat.common.user.service.impl;

import com.hitsz.badboyChat.common.chat.domain.vo.resp.GroupMemberListResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.RoomDetailResp;
import com.hitsz.badboyChat.common.chat.enums.UserRoomRoleEnum;
import com.hitsz.badboyChat.common.chat.service.adapter.ChatAdapter;
import com.hitsz.badboyChat.common.chat.service.dao.GroupMemBerDao;
import com.hitsz.badboyChat.common.chat.service.dao.RoomGroupDao;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.user.dao.RoomDao;
import com.hitsz.badboyChat.common.user.dao.RoomFriendDao;
import com.hitsz.badboyChat.common.user.dao.UserDao;
import com.hitsz.badboyChat.common.user.domain.entity.*;
import com.hitsz.badboyChat.common.user.service.RoomService;
import com.hitsz.badboyChat.common.user.service.adapter.FriendAdapter;
import com.hitsz.badboyChat.common.user.service.cache.RoomGroupCache;
import com.hitsz.badboyChat.common.user.service.cache.UserCache;
import com.hitsz.badboyChat.common.user.service.cache.UserInfoCache;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
    @Autowired
    private RoomGroupDao roomGroupDao;
    @Autowired
    private UserCache userCache;
    @Autowired
    private GroupMemBerDao groupMemBerDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private RoomGroupCache roomGroupCache;

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

    @Override
    public RoomDetailResp getRoomDetail(Long roomId, Long uid) {
        RoomGroup roomGroup = roomGroupDao.getRoomGroupByRoomId(roomId);
        Room room = roomDao.getById(roomId);
        AssertUtil.isNotEmpty(roomGroup,"房间不存在");
        Long onlineNumber;
        if(room.isHotRoom()){
            onlineNumber = userCache.getOnlineNumber();
        }else{
            List<Long> groupMembersUid = groupMemBerDao.getGroupMembersUid(roomId);
            onlineNumber = userDao.getOnlineMembers(groupMembersUid).longValue();
        }
        UserRoomRoleEnum userRoomRoleEnum = getUserRoomRole(roomGroup, uid, room);
        return RoomDetailResp.builder()
                .name(roomGroup.getName())
                .avatar(roomGroup.getAvatar())
                .role(userRoomRoleEnum.getCode())
                .onlineNumber(onlineNumber.intValue())
                .roomId(roomId)
                .build();
    }

    @Override
    public List<GroupMemberListResp> getMemberList(Long roomId) {
        RoomGroup roomGroup = roomGroupCache.getGroupByRoomId(roomId);
        Room room = roomDao.getById(roomId);
        AssertUtil.isNotEmpty(roomGroup, "房间不存在");
        if(room.isHotRoom()){
            // 只显示前100个
            List<User> users = userDao.getMembers();
            return ChatAdapter.buildGroupMemberListResp(users);
        }else{
            List<Long> groupMembersUid = groupMemBerDao.getGroupMembersUid(roomGroup.getId());
            Map<Long, User> memberInfo = userInfoCache.getBatch(groupMembersUid);
            return ChatAdapter.buildGroupMemberListResp(memberInfo);
        }
    }

    private UserRoomRoleEnum getUserRoomRole(RoomGroup roomGroup, Long uid, Room room) {
        GroupMember groupMember = Objects.isNull(uid) ? null : groupMemBerDao.getMember(roomGroup.getId(), uid);
        if(Objects.nonNull(groupMember)){
            return UserRoomRoleEnum.of(groupMember.getRole());
        }else if (room.isHotRoom()){
            return UserRoomRoleEnum.USER;
        }else{
            return UserRoomRoleEnum.REMOVED;
        }
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





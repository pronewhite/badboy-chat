package com.hitsz.badboyChat.common.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.hitsz.badboyChat.common.annotation.RedissionLock;
import com.hitsz.badboyChat.common.chat.domain.vo.req.*;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.GroupMemberListResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.RoomDetailResp;
import com.hitsz.badboyChat.common.chat.enums.UserRoomRoleEnum;
import com.hitsz.badboyChat.common.chat.service.adapter.ChatAdapter;
import com.hitsz.badboyChat.common.chat.service.dao.GroupMemBerDao;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.chat.service.dao.RoomGroupDao;
import com.hitsz.badboyChat.common.chat.service.impl.ChatServiceImpl;
import com.hitsz.badboyChat.common.constant.GroupConstant;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.enums.RoomTypeEnum;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.event.GroupAddMemberEvent;
import com.hitsz.badboyChat.common.user.dao.ContactDao;
import com.hitsz.badboyChat.common.user.dao.RoomDao;
import com.hitsz.badboyChat.common.user.dao.RoomFriendDao;
import com.hitsz.badboyChat.common.user.dao.UserDao;
import com.hitsz.badboyChat.common.user.domain.entity.*;
import com.hitsz.badboyChat.common.user.service.RoomService;
import com.hitsz.badboyChat.common.user.service.adapter.FriendAdapter;
import com.hitsz.badboyChat.common.user.service.cache.GroupMemberCache;
import com.hitsz.badboyChat.common.user.service.cache.RoomGroupCache;
import com.hitsz.badboyChat.common.user.service.cache.UserCache;
import com.hitsz.badboyChat.common.user.service.cache.UserInfoCache;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.ChatMemberResp;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBaseResp;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSMemberChange;
import com.hitsz.badboyChat.common.websocket.service.PushService;
import com.hitsz.badboyChat.common.websocket.service.adapter.WebSocketAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private PushService pushService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ChatServiceImpl chatService;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private ContactDao contactDao;

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

    @Override
    public void removeUser(RemoveUserReq req, Long uid) {
        RoomGroup group = roomGroupCache.getGroupByRoomId(req.getRoomId());
        AssertUtil.isNotEmpty(group, "房间不存在");
        GroupMember groupMember = groupMemBerDao.getMember(group.getId(), req.getUid());
        AssertUtil.isNotEmpty(groupMember, "用户不在房间中");
        GroupMember self = groupMemBerDao.getMember(group.getId(), uid);
        AssertUtil.isNotEmpty(self, "抱歉，您尚未加入该群聊");
        // 校验要删除的用户是否是群主，群主不可删除
        AssertUtil.notEqual(groupMember.getRole(), UserRoomRoleEnum.LEADER.getCode(), "暂无权限");
        if(groupMember.getRole() == UserRoomRoleEnum.ADMIN.getCode()){
            // 要删除的用户时管理员，那么需要当前用户必须是群主才能删除
            AssertUtil.equal(self.getRole(), UserRoomRoleEnum.LEADER.getCode(), "抱歉，您无权限删除管理员");
        }else{
            // 表示删除的用户是普通成员，那么当前用户必须是管理员或者群主才能删除
            AssertUtil.isTrue(self.getRole() == UserRoomRoleEnum.ADMIN.getCode() || self.getRole() == UserRoomRoleEnum.LEADER.getCode(), "抱歉，您无权限删除群成员");
        }
        groupMemBerDao.removeById(groupMember.getId());
        // 向群成员发消息通知已经有用户被移除群聊
        List<GroupMember> memberList = groupMemberCache.getMemberList(group.getId());
        WSBaseResp<WSMemberChange> memberChangeWSBaseResp = WebSocketAdapter.buildMemberChangeResp(group.getRoomId(), groupMember);
        pushService.sendPushMsg(memberChangeWSBaseResp, memberList.stream().map(GroupMember::getId).collect(Collectors.toList()));
        groupMemberCache.evictMemberUidList(group.getId());
    }

    @Override
    @RedissionLock(key = "#req.roomId")
    @Transactional(rollbackFor = Exception.class)
    public void addUser(AddUserReq req, Long uid) {
        Room room = roomDao.getById(req.getRoomId());
        AssertUtil.isFalse(room.isHotRoom(), "全员群无需邀请好友");
        RoomGroup roomGroup = roomGroupDao.getRoomGroupByRoomId(req.getRoomId());
        AssertUtil.isNotEmpty(roomGroup,"该群聊不存在");
        GroupMember currentMember = groupMemBerDao.getMember(roomGroup.getId(), uid);
        AssertUtil.isNotEmpty(currentMember,"您不是群成员，无法添加用户");
        // 取出群聊中的用户id
        List<Long> membersId = groupMemberCache.getMemberList(roomGroup.getId()).stream().map(GroupMember::getId).collect(Collectors.toList());
        Set<Long> existed = new HashSet<>(membersId);
        // 判断邀请的用户是否已经在群聊中
        List<Long> toAddUids = req.getUid().stream().filter(a -> !existed.contains(a)).distinct().collect(Collectors.toList());
        if(CollectionUtil.isEmpty(toAddUids)){
            return;
        }
        List<GroupMember> groupMembers = ChatAdapter.buildAddUserResp(toAddUids, roomGroup.getId());
        groupMemBerDao.saveBatch(groupMembers);
        // 发送添加用户的事件
        applicationEventPublisher.publishEvent(new GroupAddMemberEvent(this, roomGroup, groupMembers, uid));
    }

    @Override
    public CursorPageBaseResp<ChatMemberResp> getMemberPage(MemberReq req) {
        Long roomId = req.getRoomId();
        Room room = roomDao.getById(roomId);
        RoomGroup group = roomGroupCache.getGroupByRoomId(roomId);
        AssertUtil.isNotEmpty(room,"房间号有误");
        AssertUtil.isNotEmpty(group,"该群聊不存在");
        List<Long> memberList;
        if(room.isHotRoom()){
            // 全员群展示全部成员
            memberList = null;
        }else{
            memberList = groupMemberCache.getMemberList(group.getId()).stream().map(GroupMember::getUid).collect(Collectors.toList());
        }
        return chatService.getMemberPage(req, memberList);
    }

    @Override
    public void exitGroup(Long roomId, Long uid) {
        Room room = roomDao.getById(roomId);
        RoomGroup roomGroup = roomGroupDao.getRoomGroupByRoomId(roomId);
        AssertUtil.isNotEmpty(roomGroup,"房间号有误");
        GroupMember member = groupMemberCache.getMember(roomGroup.getId(), uid);
        AssertUtil.isNotEmpty(member,"您尚未加入该群聊");
        // 当前用户是否是群主，群主退出群聊意味着解散群聊
        if(UserRoomRoleEnum.LEADER.getCode().equals(member.getRole())){
            // 群主退出群聊，解散群聊
            dismissGroup(roomId, roomGroup.getId());
        }else{
            // 非群主退出群聊
            groupMemBerDao.removeMember(roomGroup.getId(), uid);
            // 删除会话
            contactDao.removeContact(roomId, uid);
            // 推送消息给群成员，有成员退出群聊
            List<Long> uidList = groupMemberCache.getMemberList(roomGroup.getId()).stream().map(GroupMember::getUid).collect(Collectors.toList());
            WSBaseResp<WSMemberChange> ws = WebSocketAdapter.buildExitGroup(roomId, uid);
            pushService.sendPushMsg(ws, uidList);
            // 删除缓存
            groupMemberCache.evictMemberUidList(roomGroup.getId());
        }
    }

    @Override
    public IdReqVO addGroup(GroupAddReq req, Long uid) {
        AssertUtil.isEmpty(uid, "您尚未登录");
        // 新建群聊
        RoomGroup roomGroup = createRoomGroup(uid);
        // 保存群成员
        List<Long> uids = req.getUids();
        List<GroupMember> members = ChatAdapter.buildAddGroupMembers(roomGroup.getId(),uids);
        groupMemBerDao.saveBatch(members);
        // 发送邀请加群消息，复用之前的接口
        applicationEventPublisher.publishEvent(new GroupAddMemberEvent(this, roomGroup, members, uid));
        return new IdReqVO(roomGroup.getRoomId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoomGroup createRoomGroup(Long uid) {
        List<GroupMember> selfGroupRoom = groupMemBerDao.getSelfGroupRoom(uid);
        AssertUtil.isEmpty(selfGroupRoom, "一个人只能创建一个群聊哦");
        // 创建群组
        RoomGroup roomGroup = new RoomGroup();
        User user = userInfoCache.get(uid);
        Room  room = new Room();
        room.setType(RoomTypeEnum.GROUP_CHAT.getCode());
        room.setHotFlag(YesOrNoEnum.NO.getCode());
        boolean save = roomDao.save(room);
        AssertUtil.isTrue(save,"创建房间失败");
        Long roomId = room.getId();
        roomGroup.setRoomId(roomId);
        roomGroup.setName(user.getName() + "的群聊");
        roomGroup.setAvatar(user.getAvatar());
        boolean saveRoomGroup = roomGroupDao.save(roomGroup);
        AssertUtil.isTrue(saveRoomGroup,"创建群聊失败");
        // 插入群主
        GroupMember leader = GroupMember.builder()
                .groupId(roomGroup.getId())
                .role(UserRoomRoleEnum.LEADER.getCode())
                .uid(uid)
                .build();
        groupMemBerDao.save(leader);
        return roomGroup;
    }

    @Override
    public void addAdmin(AdminAddOrRemoveReq req, Long uid) {
        AssertUtil.isNotEmpty(uid, "您尚未登录");
        Room room = roomDao.getById(req.getRoomId());
        RoomGroup roomGroup = roomGroupDao.getRoomGroupByRoomId(room.getId());
        AssertUtil.isNotEmpty(roomGroup, "房间号有误");
        GroupMember currentMember = groupMemBerDao.getMember(roomGroup.getId(), uid);
        // 判断当前用户是否有权限，只有群主能添加管理员
        AssertUtil.equal(currentMember.getRole(), UserRoomRoleEnum.LEADER.getCode(), "只有群主能添加管理员哦");
        // 判断要添加为管理员的用户是否在群聊中
        List<Long> membersIds = groupMemberCache.getMemberList(roomGroup.getId()).stream().map(GroupMember::getUid).distinct().collect(Collectors.toList());
        Set<Long> toAddUidSet = req.getUids().stream().filter(id -> !membersIds.contains(id)).collect(Collectors.toSet());
        if(CollectionUtil.isEmpty(toAddUidSet)){
            return;
        }
        // 判断群管理员数量是否已经达到要求
        Set<Long> adminUids = groupMemBerDao.getAdminCount(roomGroup.getId());
        adminUids.addAll(toAddUidSet);
        AssertUtil.isTrue(adminUids.size() < GroupConstant.MAX_ADMIN_COUNT, "群管理员数量已达上限");
        // 添加群管理员
        groupMemBerDao.addAdmin(roomGroup.getId(), new ArrayList<>(adminUids));
    }

    @Override
    public void removeAdmin(AdminAddOrRemoveReq req, Long uid) {
        AssertUtil.isNotEmpty(uid, "您尚未登录");
        Room room = roomDao.getById(req.getRoomId());
        RoomGroup roomGroup = roomGroupDao.getRoomGroupByRoomId(room.getId());
        AssertUtil.isNotEmpty(roomGroup, "房间号有误");
        GroupMember currentMember = groupMemBerDao.getMember(roomGroup.getId(), uid);
        // 判断当前用户是否有权限，只有群主能移除管理员
        AssertUtil.equal(currentMember.getRole(), UserRoomRoleEnum.LEADER.getCode(), "只有群主能移除管理员哦");
        // 判断要撤销的管理员是否在群内
        boolean inGroupRoom = groupMemBerDao.isInGroupRoom(roomGroup.getId(), req.getUids());
        AssertUtil.isTrue(inGroupRoom, "请确保您要撤销的管理员在群内");
        // 移除管理员
        groupMemBerDao.removeAdmin(roomGroup.getId(), req.getUids());
    }

    private void dismissGroup(Long roomId, Long groupId) {
        // 删除房间
        roomDao.removeById(roomId);
        // 删除会话
        contactDao.removeContact(roomId);
        // 删除群成员
        groupMemBerDao.removeMembers(groupId);
        // 删除群组
        roomGroupDao.removeById(groupId);
        // 删除群消息
        messageDao.removeMessages(roomId);
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





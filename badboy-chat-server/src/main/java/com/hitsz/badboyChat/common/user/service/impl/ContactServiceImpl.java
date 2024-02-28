package com.hitsz.badboyChat.common.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.chat.domain.dto.RoomBaseInfo;
import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatRoomResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.MsgReadInfoResp;
import com.hitsz.badboyChat.common.chat.service.adapter.ChatAdapter;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.chat.service.factory.MsgHandlerFactory;
import com.hitsz.badboyChat.common.chat.service.strategy.AbstractMsgHandler;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.enums.RoomTypeEnum;
import com.hitsz.badboyChat.common.user.dao.ContactDao;
import com.hitsz.badboyChat.common.user.dao.RoomDao;
import com.hitsz.badboyChat.common.user.domain.entity.*;
import com.hitsz.badboyChat.common.user.mapper.ContactMapper;
import com.hitsz.badboyChat.common.user.service.ContactService;
import com.hitsz.badboyChat.common.user.service.RoomService;
import com.hitsz.badboyChat.common.user.service.cache.*;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import com.hitsz.badboyChat.common.user.utils.CursorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author lenovo
* @description 针对表【contact(会话列表)】的数据库操作Service实现
* @createDate 2024-02-07 13:26:12
*/
@Service
public class ContactServiceImpl implements  ContactService{

    @Autowired
    private ContactDao contactDao;
    @Autowired
    private HotRoomCache hotRoomCache;
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private RoomGroupCache roomGroupCache;
    @Autowired
    private RoomFriendCache roomFriendCache;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomService roomService;

    @Override
    public CursorPageBaseResp<ChatRoomResp> getChatRooms(Long uid, CursorPageBaseReq req) {
        CursorPageBaseResp<Long> page;
        // 用户登录与未登录看到的会话列表是不一致的
        if(Objects.nonNull(uid)){
            // 用户已登录
            Double hotEnd = getCursorOrNull(req.getCursor());
            Double hotStart = null;
            // 获取基础会话
            CursorPageBaseResp<Contact> contacts = contactDao.getContacts(uid, req);
            // 获取会话房间id
            List<Long> chatRoomIds = contacts.getList().stream().map(Contact::getRoomId).collect(Collectors.toList());
            if(!contacts.getIsLast()){
                hotStart = getCursorOrNull(contacts.getCursor());
            }
            // 获取热点群聊房间
            Set<ZSetOperations.TypedTuple<String>> hotRooms = hotRoomCache.getHotRooms(hotStart, hotEnd);
            // 获取热点群聊的房间id
            List<Long> hotChatRoomIds = hotRooms.stream().map(ZSetOperations.TypedTuple::getValue).map(Long::parseLong).collect(Collectors.toList());
            // 聚合
            chatRoomIds.addAll(hotChatRoomIds);
            page = CursorPageBaseResp.init(contacts, chatRoomIds);
        }else{
            // 用户未登录
            // 未登录只获取热点群聊房间
            CursorPageBaseResp<Pair<Long, Double>> roomCursorPage = hotRoomCache.getRoomCursorPage(req);
            List<Long> roomIds = roomCursorPage.getList().stream().map(Pair::getKey).collect(Collectors.toList());
            page = CursorPageBaseResp.init(roomCursorPage, roomIds);
        }
        List<ChatRoomResp>  respList = buildChatRoomResp(page.getList(), uid);
        return CursorPageBaseResp.init(page, respList);
    }

    private List<ChatRoomResp> buildChatRoomResp(List<Long> roomList, Long uid) {
        Map<Long, RoomBaseInfo> roomBaseInfoMap = getRoomBaseInfoMap(roomList, uid);
        // 取出房间的最后一条消息id
        List<Long> lastMsgIds = roomBaseInfoMap.values().stream().map(RoomBaseInfo::getLastMsgId).collect(Collectors.toList());
        List<Message> msgs = messageDao.getMsgSByIds(lastMsgIds);
        Map<Long, Message> msgMap = msgs.stream().collect(Collectors.toMap(Message::getId, Function.identity()));
        Map<Long, User> lastMsgUserMap = userInfoCache.getBatch(msgs.stream().map(Message::getFromUid).collect(Collectors.toList()));
        Map<Long, Integer> unReadCountMap = contactDao.getUnReadCount(uid, roomList);
        return roomList.stream().map(roomId -> {
            ChatRoomResp chatRoomResp = new  ChatRoomResp();
            chatRoomResp.setRoomId(roomId);
            RoomBaseInfo roomBaseInfo = roomBaseInfoMap.get(roomId);
            chatRoomResp.setType(roomBaseInfo.getType());
            chatRoomResp.setHotFlag(roomBaseInfo.getHotFlag());
            chatRoomResp.setName(roomBaseInfo.getName());
            chatRoomResp.setAvatar(roomBaseInfo.getAvatar());
            Message message = msgMap.get(roomBaseInfo.getLastMsgId());
            if(Objects.nonNull(message)){
                AbstractMsgHandler msgHandler = MsgHandlerFactory.getStrategyOrNull(message.getType());
                chatRoomResp.setText(lastMsgUserMap.get(message.getFromUid()) + ":" + msgHandler.showContactMsg(message));
            }
            chatRoomResp.setActiveTime(roomBaseInfo.getActiveTime());
            chatRoomResp.setUnreadCount(unReadCountMap.get(roomId));
            return  chatRoomResp;
        }).sorted(Comparator.comparing(ChatRoomResp::getActiveTime).reversed())// 会话展示顺序按照时间降序排序
                .collect(Collectors.toList());
    }

    private Map<Long, RoomBaseInfo> getRoomBaseInfoMap(List<Long> roomList, Long uid) {
        Map<Long, Room> batch = roomCache.getBatch(roomList);
        Map<Integer, List<Long>> roomTypeMap = batch.values().stream()
                .collect(Collectors.groupingBy(Room::getType, Collectors.mapping(Room::getId, Collectors.toList())));
        // 取出群聊类型的房间
        List<Long> groupRoomList = roomTypeMap.get(RoomTypeEnum.GROUP_CHAT.getCode());
        // 获取群聊房间的详情
        Map<Long, RoomGroup> groupMap = roomGroupCache.getBatch(groupRoomList);
        // 取出单聊类型房间
        List<Long> singleRoomList = roomTypeMap.get(RoomTypeEnum.SINGLE_CHAT.getCode());
        // 获取单聊房间的详情
        Map<Long, User> userFriendInfoMap = getUserFriendInfo(singleRoomList, uid);
        // 组装信息
        return batch.values().stream().map(room -> {
            RoomBaseInfo roomBaseInfo = new RoomBaseInfo();
            roomBaseInfo.setRoomId(room.getId());
            roomBaseInfo.setHotFlag(room.getHotFlag());
            roomBaseInfo.setActiveTime(room.getActiveTime());
            roomBaseInfo.setType(room.getType());
            roomBaseInfo.setLastMsgId(room.getLastMsgId());
            if(room.getType() == RoomTypeEnum.GROUP_CHAT.getCode()){
                RoomGroup roomGroup = groupMap.get(room.getId());
                roomBaseInfo.setAvatar(roomGroup.getAvatar());
                roomBaseInfo.setName(roomGroup.getName());
            }else {
                User user = userFriendInfoMap.get(room.getId());
                roomBaseInfo.setAvatar(user.getAvatar());
                roomBaseInfo.setName(user.getName());
            }
            return roomBaseInfo;
        }).collect(Collectors.toMap(RoomBaseInfo::getRoomId, Function.identity()));
    }

    private Map<Long, User> getUserFriendInfo(List<Long> singleRoomList, Long uid) {
        if(CollectionUtils.isEmpty(singleRoomList)){
            return new HashMap<>();
        }
        Map<Long, RoomFriend> batch = roomFriendCache.getBatch(singleRoomList);
        Set<Long> friendUidSet = ChatAdapter.getFriendUidSet(batch.values(), uid);
        Map<Long, User> userInfoMap = userInfoCache.getBatch(new ArrayList<>(friendUidSet));
        return batch.values().stream()
                .collect(Collectors.toMap(RoomFriend::getRoomId, roomFriend -> {
                    Long friendUid = ChatAdapter.getFriendUid(roomFriend, uid);
                    return userInfoMap.get(friendUid);
                }));
    }

    private Double getCursorOrNull(String cursor) {
        return Optional.ofNullable(cursor).map(Double::parseDouble).orElse(null);
    }

    @Override
    public Collection<MsgReadInfoResp> getMsgReadInfo(Long uid, List<Message> msgs) {
        Map<Long, List<Message>> collect = msgs.stream().collect(Collectors.groupingBy(Message::getRoomId));
        AssertUtil.equal(collect.size(), 1, "只能查询一个房间中的消息已读未读数");
        Long roomId = collect.keySet().iterator().next();
        Integer totalCount = contactDao.getTotalCount(roomId);
        return msgs.stream().map(msg -> {
            MsgReadInfoResp resp = new MsgReadInfoResp();
            resp.setMsgId(msg.getId());
            Integer readCount = contactDao.getReadCount(msg);
            resp.setReadCount(readCount);
            Integer unReadCount = totalCount - readCount;
            resp.setUnReadCount(unReadCount);
            return resp;
        }).collect(Collectors.toList());

    }

    @Override
    public ChatRoomResp getContactDetail(Long roomId, Long uid) {
        Room room = roomDao.getById(roomId);
        AssertUtil.isNotEmpty(room, "房间号有误");
        return buildChatRoomResp(Collections.singletonList(roomId), uid).get(0);
    }

    @Override
    public ChatRoomResp getContactDetailFriend(Long friendUid, Long uid) {
        RoomFriend roomFriend = roomService.createRoomFriend(Arrays.asList(friendUid, uid));
        AssertUtil.isNotEmpty(roomFriend, "好友关系不存在");
        return buildChatRoomResp(Collections.singletonList(roomFriend.getRoomId()), uid).get(0);
    }
}





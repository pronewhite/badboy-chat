package com.hitsz.badboyChat.common.chat.service.impl;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.event.MessageSendEvent;
import com.hitsz.badboyChat.common.chat.service.ChatService;
import com.hitsz.badboyChat.common.chat.service.dao.GroupMemBerDao;
import com.hitsz.badboyChat.common.chat.service.dao.RoomGroupDao;
import com.hitsz.badboyChat.common.chat.service.factory.MsgHandlerFactory;
import com.hitsz.badboyChat.common.chat.service.strategy.AbstractMsgHandler;
import com.hitsz.badboyChat.common.enums.CommonErrorEnum;
import com.hitsz.badboyChat.common.user.dao.RoomFriendDao;
import com.hitsz.badboyChat.common.user.domain.entity.GroupMember;
import com.hitsz.badboyChat.common.user.domain.entity.Room;
import com.hitsz.badboyChat.common.user.domain.entity.RoomFriend;
import com.hitsz.badboyChat.common.user.domain.entity.RoomGroup;
import com.hitsz.badboyChat.common.user.service.cache.RoomCache;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 12:34
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private RoomCache roomCache;
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private RoomGroupDao roomGroupDao;
    @Autowired
    private GroupMemBerDao groupMemBerDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void chat(Long uid, ChatMessageReq req) {
        checkMsgValid(req, uid);
        AbstractMsgHandler msgHandler = MsgHandlerFactory.getStrategyOrNull(req.getMsgType());
        AssertUtil.isNotEmpty(msgHandler, CommonErrorEnum.PARAM_INVALID);
        Long msgId = msgHandler.checkAndSaveMsg(req, uid);
        // 推送发送消息的事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this,msgId));
    }

    private void checkMsgValid(ChatMessageReq req, Long uid) {
        Room room = roomCache.get(req.getRoomId());
        // 如果是热点群聊直接跳过检验
        if (room.isHotRoom()) {
            return;
        }
        if (room.isRoomFriend()) {
            RoomFriend roomFriend = roomFriendDao.getByRoomId(req.getRoomId());
            AssertUtil.isNotEmpty(roomFriend, "你已经被删除好友了，无法发送消息哦");
        }
        if(room.isRoomGroup()){
            RoomGroup roomGroup = roomGroupDao.getRoomGroupById(req.getRoomId());
            GroupMember member = groupMemBerDao.getMember(roomGroup.getId(), uid);
            AssertUtil.isNotEmpty(member,"你已经被删除群聊了，无法发送消息哦");
        }
    }
}

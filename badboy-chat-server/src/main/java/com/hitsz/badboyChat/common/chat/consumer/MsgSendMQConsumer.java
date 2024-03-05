package com.hitsz.badboyChat.common.chat.consumer;

import com.hitsz.badboyChat.common.chat.domain.dto.MsgSendMQDTO;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
import com.hitsz.badboyChat.common.chat.service.ChatService;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.constant.MQConstant;
import com.hitsz.badboyChat.common.enums.RoomTypeEnum;
import com.hitsz.badboyChat.common.user.dao.ContactDao;
import com.hitsz.badboyChat.common.user.dao.RoomDao;
import com.hitsz.badboyChat.common.user.dao.RoomFriendDao;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.domain.entity.Room;
import com.hitsz.badboyChat.common.user.domain.entity.RoomFriend;
import com.hitsz.badboyChat.common.user.service.cache.HotRoomCache;
import com.hitsz.badboyChat.common.user.service.cache.RoomCache;
import com.hitsz.badboyChat.common.user.service.cache.RoomGroupCache;
import com.hitsz.badboyChat.common.websocket.service.PushService;
import com.hitsz.badboyChat.common.websocket.service.adapter.WebSocketAdapter;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 16:34
 */
@RocketMQMessageListener(consumerGroup = MQConstant.MQ_SEND_MSG_GROUP, topic = MQConstant.MQ_SEND_MSG_TOPIC)
@Component
public class MsgSendMQConsumer implements RocketMQListener<MsgSendMQDTO> {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private ChatService chatService;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private HotRoomCache hotRoomCache;
    @Autowired
    private PushService pushService;
    @Autowired
    private RoomGroupCache roomGroupCache;
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private ContactDao contactDao;

    @Override
    public void onMessage(MsgSendMQDTO msgSendMQDTO) {
        Message message = messageDao.getById(msgSendMQDTO.getMsgId());
        Room room = roomCache.get(message.getRoomId());
        ChatMessageResp chatMessageResp = chatService.getChatResp(message, null);
        // 更新房间最新消息
        roomDao.refreshRoomMessage(room.getId(), message.getId(), message.getCreateTime());
        // 删除缓存
        roomCache.delete(room.getId());
        List<Long> memberList = new ArrayList<>();
        // 如果是热门群聊，则推送消息给所有在线的群成员，不在线群成员不推送
        if (room.isHotRoom()) {
            // 更新热门群聊的最新消息时间
            hotRoomCache.refreshHotRoomActiveTime(room.getId(), message.getCreateTime());
            // 推送消息给所有在线的群成员
            pushService.sendPushMsg(WebSocketAdapter.buildPushMsg(chatMessageResp));
        } else {
            if(Objects.equals(room.getType(), RoomTypeEnum.GROUP_CHAT.getCode())){
                memberList = roomGroupCache.getRoomGrouopMembersUid(room.getId());
            }else if(Objects.equals(room.getType(), RoomTypeEnum.SINGLE_CHAT.getCode())){
                RoomFriend roomFriend = roomFriendDao.getByRoomId(room.getId());
                memberList = Arrays.asList(roomFriend.getUid1(), roomFriend.getUid2());
            }
        }
        // 更新所有群成员的会话时间
        contactDao.refreshOrCreateActiveTime(room.getId(), memberList,message.getId(), message.getCreateTime());
        // 向所有群成员推送消息
        pushService.sendPushMsg(WebSocketAdapter.buildPushMsg(chatMessageResp), memberList);
    }
}

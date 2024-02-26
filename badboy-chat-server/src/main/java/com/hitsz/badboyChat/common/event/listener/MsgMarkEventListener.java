package com.hitsz.badboyChat.common.event.listener;

import com.hitsz.badboyChat.common.chat.domain.dto.ChatMessageMarkDTO;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.chat.enums.MsgMarkTypeEnum;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.enums.IdempotentEnum;
import com.hitsz.badboyChat.common.enums.ItemEnum;
import com.hitsz.badboyChat.common.event.MsgMarkEvent;
import com.hitsz.badboyChat.common.user.dao.MessageMarkDao;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.domain.entity.MessageMark;
import com.hitsz.badboyChat.common.user.service.UserService;
import com.hitsz.badboyChat.common.websocket.service.PushService;
import com.hitsz.badboyChat.common.websocket.service.adapter.WebSocketAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/25 16:42
 */
@Slf4j
@Component
public class MsgMarkEventListener {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private MessageMarkDao messageMarkDao;
    @Autowired
    private UserService userService;
    @Autowired
    private PushService pushService;

    @Async
    @TransactionalEventListener(fallbackExecution = true, classes = MsgMarkEvent.class)
    public void changeMsgType(MsgMarkEvent event){
        ChatMessageMarkDTO chatMessageMarkDTO = event.getChatMessageMarkDTO();
        Message message = messageDao.getById(chatMessageMarkDTO.getMsgId());
        if(! MessageTypeEnum.TEXT.equals(message.getType())){
            return;
        }
        // 判断是否需要给用户发放徽章，消息标记数满足要求即可发放徽章
        Integer msgMarkCount = messageMarkDao.getMsgMarkCount(chatMessageMarkDTO.getMsgId(), chatMessageMarkDTO.getMarkType());
        MsgMarkTypeEnum markTypeEnum = MsgMarkTypeEnum.of(chatMessageMarkDTO.getMarkType());
        // 不满足发放徽章要求，直接返回
        if(msgMarkCount < markTypeEnum.getCount()){
            return;
        }
        // 如果是点赞，发放点赞徽章 todo：后续可以发放点踩徽章
        if(Objects.equals(MsgMarkTypeEnum.LIKE.getCode(), chatMessageMarkDTO.getMarkType())){
            userService.acquireItem(message.getFromUid(), ItemEnum.LIKE_BADGE.getId(), IdempotentEnum.MSG_ID,message.getId().toString());
        }
    }

    @TransactionalEventListener(classes = MsgMarkEvent.class, fallbackExecution = true)
    public void pushToAll(MsgMarkEvent event){
        ChatMessageMarkDTO dto = event.getChatMessageMarkDTO();
        // 推送消息标记给所有用户
        Integer msgMarkCount = messageMarkDao.getMsgMarkCount(dto.getMsgId(), dto.getMarkType());
        pushService.sendPushMsg(WebSocketAdapter.buildMsgMarkPushMsg(dto, msgMarkCount));
    }

}

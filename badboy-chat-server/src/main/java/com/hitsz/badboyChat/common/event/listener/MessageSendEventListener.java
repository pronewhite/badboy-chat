package com.hitsz.badboyChat.common.event.listener;

import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.event.MessageSendEvent;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 14:19
 */
@Component
@Slf4j
public class MessageSendEventListener {

    @Autowired
    private MessageDao messageDao;

    @EventListener(classes = MessageSendEvent.class)
    public void msgRoute(MessageSendEvent event){
        Message message = messageDao.getById(event.getMsgId());
        //TODO  MQ推送消息

    }
}

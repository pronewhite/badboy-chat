package com.hitsz.badboyChat.common.event.listener;

import com.hitsz.badboyChat.common.chat.domain.dto.MsgSendMQDTO;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.constant.MQConstant;
import com.hitsz.badboyChat.common.event.MessageSendEvent;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboychat.transaction.service.MQProducer;
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
    @Autowired
    private MQProducer mqProducer;

    @EventListener(classes = MessageSendEvent.class)
    public void msgRoute(MessageSendEvent event){
        Message message = messageDao.getById(event.getMsgId());
        mqProducer.sendSecureMsg(MQConstant.MQ_SEND_MSG_TOPIC, new MsgSendMQDTO(message.getId()), message.getId());
    }
}

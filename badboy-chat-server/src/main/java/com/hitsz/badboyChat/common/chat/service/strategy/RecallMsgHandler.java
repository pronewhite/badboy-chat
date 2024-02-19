package com.hitsz.badboyChat.common.chat.service.strategy;

import com.hitsz.badboyChat.common.chat.domain.dto.ChatMsgRecallDTO;
import com.hitsz.badboyChat.common.chat.domain.dto.MsgRecall;
import com.hitsz.badboyChat.common.chat.domain.entity.msg.MessageExtra;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.event.MsgRecallEvent;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/18 0:02
 */
@Component
public class RecallMsgHandler extends AbstractMsgHandler<Object>{

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    protected MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.RECALL;
    }

    @Override
    protected void saveMsg(Message insert, Object body) {

    }

    @Override
    public Object showMsg(Message message) {
        return null;
    }

    public void recallMsg(Message message, Long uid) {
        // 将撤回抽象为一种消息，所以撤回某条消息实质上就是将该消息类型修改为撤回类型
        MessageExtra extra = message.getExtra();
        extra.setRecall(new MsgRecall(uid, new Date()));
        message.setExtra(extra);
        message.setType(MessageTypeEnum.RECALL.getType());
        messageDao.updateById(message);
        // 推送撤回消息的事件
        applicationEventPublisher.publishEvent(new MsgRecallEvent(this, new ChatMsgRecallDTO(message.getId(), uid, message.getRoomId())));
    }
}

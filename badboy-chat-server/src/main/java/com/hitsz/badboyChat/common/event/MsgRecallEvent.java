package com.hitsz.badboyChat.common.event;

import com.hitsz.badboyChat.common.chat.domain.dto.ChatMsgRecallDTO;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/18 0:15
 */
@Getter
public class MsgRecallEvent extends ApplicationEvent {

    public ChatMsgRecallDTO chatMsgRecallDTO;

    public MsgRecallEvent(Object source, ChatMsgRecallDTO chatMsgRecallDTO) {
        super(source);
        this.chatMsgRecallDTO  = chatMsgRecallDTO;
    }
}

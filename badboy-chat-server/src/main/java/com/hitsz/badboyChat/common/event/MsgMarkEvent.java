package com.hitsz.badboyChat.common.event;

import com.hitsz.badboyChat.common.chat.domain.dto.ChatMessageMarkDTO;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/25 16:40
 */
@Getter
public class MsgMarkEvent extends ApplicationEvent {

    public ChatMessageMarkDTO chatMessageMarkDTO;

    public MsgMarkEvent(Object source, ChatMessageMarkDTO chatMessageMarkDTO) {
        super(source);
        this.chatMessageMarkDTO = chatMessageMarkDTO;
    }
}

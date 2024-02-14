package com.hitsz.badboyChat.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 14:17
 */
@Getter
public class MessageSendEvent extends ApplicationEvent {

    public Long msgId;

    public MessageSendEvent(Object source, Long msgId) {
        super(source);
        this.msgId = msgId;
    }
}

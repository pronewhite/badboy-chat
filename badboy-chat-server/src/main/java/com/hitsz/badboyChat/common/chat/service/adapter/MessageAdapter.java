package com.hitsz.badboyChat.common.chat.service.adapter;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.enums.MessageStatusEnum;
import com.hitsz.badboyChat.common.user.domain.entity.Message;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 11:38
 */
public class MessageAdapter {
    public static Message buildMessageInsert(ChatMessageReq req, Long uid) {
        return Message.builder()
                .fromUid(uid)
                .roomId(req.getRoomId())
                .type(req.getMsgType())
                .status(MessageStatusEnum.NORAML.getCode())
                .build();
    }
}

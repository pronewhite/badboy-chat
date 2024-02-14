package com.hitsz.badboyChat.common.chat.service;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;

public interface ChatService {
    /**
     * 聊天接口
     * @param uid 发送消息的用户id
     * @param req 发送的消息的信息
     */
    void chat(Long uid, ChatMessageReq req);
}

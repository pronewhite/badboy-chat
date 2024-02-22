package com.hitsz.badboyChat.common.chat.service;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.req.GetMessagePageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.req.MsgCallbackReq;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.domain.entity.Message;

public interface ChatService {
    /**
     * 聊天接口
     * @param uid 发送消息的用户id
     * @param req 发送的消息的信息
     */
    Long chat(Long uid, ChatMessageReq req);

    /**
     *  获取前端展示的消息信息
     * @param message 消息
     * @param reciveUid 目标用户id
     * @return
     */
    ChatMessageResp getChatResp(Message message, Long reciveUid);

    /**
     * 获取消息列表
     * @param uid
     * @param req
     * @return
     */
    CursorPageBaseResp<ChatMessageResp> getMsgPage(Long uid, GetMessagePageReq req);

    /**
     * 撤回消息
     * @param uid
     * @param req
     */
    void msgCallback(Long uid, MsgCallbackReq req);

    /**
     * 获取发送消息的展示详情
     * @param msgId
     * @param uid
     * @return
     */
    ChatMessageResp getMsgResp(Long msgId, Long uid);
}

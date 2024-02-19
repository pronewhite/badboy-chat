package com.hitsz.badboyChat.common.websocket.service;

import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBaseResp;

import java.util.List;

/**
 * 推送消息的服务
 */
public interface PushService {
    void sendPushMsg(WSBaseResp<?> wsBaseResp);
    void sendPushMsg(WSBaseResp<?> wsBaseResp, List<Long> uidList);
}

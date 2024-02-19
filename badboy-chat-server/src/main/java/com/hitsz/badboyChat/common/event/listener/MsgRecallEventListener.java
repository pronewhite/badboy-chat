package com.hitsz.badboyChat.common.event.listener;

import com.hitsz.badboyChat.common.chat.domain.dto.ChatMsgRecallDTO;
import com.hitsz.badboyChat.common.event.MsgRecallEvent;
import com.hitsz.badboyChat.common.user.service.cache.MsgCache;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBaseResp;
import com.hitsz.badboyChat.common.websocket.service.PushService;
import com.hitsz.badboyChat.common.websocket.service.adapter.WebSocketAdapter;
import com.hitsz.badboychat.transaction.service.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/18 0:17
 */
@Component
public class MsgRecallEventListener {

    @Autowired
    private PushService pushService;
    @Autowired
    private MsgCache msgCache;

    @EventListener(classes = MsgRecallEvent.class)
    public void evitMsgCache(MsgRecallEvent event){
        ChatMsgRecallDTO chatMsgRecallDTO = event.getChatMsgRecallDTO();
        // 清除撤回消息的缓存
        msgCache.evitMsgCache(chatMsgRecallDTO.getMsgId());
    }

    @EventListener(classes = MsgRecallEvent.class)
    public void sendToAll(MsgRecallEvent event){
        ChatMsgRecallDTO chatMsgRecallDTO = event.getChatMsgRecallDTO();
        pushService.sendPushMsg(WebSocketAdapter.buildMsgRecall(chatMsgRecallDTO));
    }
}

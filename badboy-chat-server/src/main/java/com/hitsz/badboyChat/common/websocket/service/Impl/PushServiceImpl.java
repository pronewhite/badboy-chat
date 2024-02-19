package com.hitsz.badboyChat.common.websocket.service.Impl;

import com.hitsz.badboyChat.common.chat.domain.dto.PushMsgDTO;
import com.hitsz.badboyChat.common.constant.MQConstant;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBaseResp;
import com.hitsz.badboyChat.common.websocket.service.PushService;
import com.hitsz.badboychat.transaction.service.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 22:14
 */
@Service
public class PushServiceImpl implements PushService {

    @Autowired
    private MQProducer mqProducer;

    @Override
    public void sendPushMsg(WSBaseResp<?> wsBaseResp){
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMsgDTO(wsBaseResp));
    }

    @Override
    public void sendPushMsg(WSBaseResp<?> wsBaseResp, List<Long> uidList) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMsgDTO(wsBaseResp, uidList));
    }
}

package com.hitsz.badboyChat.common.user.consumer;

import com.hitsz.badboyChat.common.chat.domain.dto.PushMsgDTO;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.chat.enums.WSPushTypeEnum;
import com.hitsz.badboyChat.common.constant.MQConstant;
import com.hitsz.badboyChat.common.websocket.service.WebSocketService;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 23:28
 */
@RocketMQMessageListener(consumerGroup = MQConstant.PUSH_GROUP,topic = MQConstant.PUSH_TOPIC, messageModel = MessageModel.BROADCASTING)
@Component
public class PushConsumer implements RocketMQListener<PushMsgDTO> {

    @Autowired
    private WebSocketService webSocketService;

    @Override
    public void onMessage(PushMsgDTO pushMsgDTO) {
        // 判断消息推送类型（全员推送还是个人推送）
        WSPushTypeEnum pushType = WSPushTypeEnum.of(pushMsgDTO.getPushType());
        switch (pushType) {
            case PERSONAL:
                pushMsgDTO.getUidList().forEach(uid -> webSocketService.pushToUser(uid, pushMsgDTO.getWsBaseResp()));
                break;
            case GROUP_MEMBER:
                webSocketService.pushToAllOnline(pushMsgDTO.getWsBaseResp(), null);
                break;
        }
    }
}

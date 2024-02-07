package com.hitsz.badboyChat.common.event.listener;

import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.event.UserBlackEvent;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.mapper.UserMapper;
import com.hitsz.badboyChat.common.websocket.domain.enums.WSRespTypeEnum;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBaseResp;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBlack;
import com.hitsz.badboyChat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/4 14:23
 */
@Component
@Slf4j
public class UserBlackEvenListener {

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserMapper userMapper;

    @EventListener(classes = UserBlackEvent.class)
    public void pushEvent(UserBlackEvent event){
        User user = event.getUser();
        WSBaseResp<WSBlack> resp = new WSBaseResp<>();
        WSBlack wsBlack = new WSBlack();
        wsBlack.setUid(user.getId());
        resp.setData(wsBlack);
        resp.setType(WSRespTypeEnum.BLACK.getType());
        webSocketService.sendMsgToAll(resp, user.getId());
    }

    @EventListener(classes = UserBlackEvent.class)
    public void modifyUserStatus(UserBlackEvent event){
        User user = event.getUser();
        user.setStatus(YesOrNoEnum.YES.getCode());
        userMapper.updateById(user);
    }
}

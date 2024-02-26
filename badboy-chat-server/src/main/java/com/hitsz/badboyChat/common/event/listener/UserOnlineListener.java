package com.hitsz.badboyChat.common.event.listener;

import com.hitsz.badboyChat.common.enums.UserActiveStatusEnum;
import com.hitsz.badboyChat.common.event.UserOnlineEvent;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.mapper.UserMapper;
import com.hitsz.badboyChat.common.user.service.IpService;
import com.hitsz.badboyChat.common.user.service.cache.UserCache;
import com.hitsz.badboyChat.common.websocket.service.PushService;
import com.hitsz.badboyChat.common.websocket.service.adapter.WebSocketAdapter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/3 19:42
 */
@Component
@Slf4j
public class UserOnlineListener {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IpService ipService;
    @Autowired
    private UserCache userCache;
    @Autowired
    private PushService pushService;
    @Autowired
    private WebSocketAdapter webSocketAdapter;

    @EventListener(classes = UserOnlineEvent.class)
    public void saveUserOnline(UserOnlineEvent event) {
        User user = event.getUser();
        userCache.online(user.getId(), user.getLastOptTime());
        pushService.sendPushMsg(webSocketAdapter.buildOnlineResp(user));
    }


    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void handlerUserOnline(UserOnlineEvent event) {
        User user = event.getUser();
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastOptTime(user.getLastOptTime());
        updateUser.setActiveStatus(UserActiveStatusEnum.ONLINE.getStatus());
        userMapper.updateById(updateUser);
        ipService.refreshIpDetailAsync(user.getId());
    }
}

package com.hitsz.badboyChat.common.event.listener;

import com.hitsz.badboyChat.common.enums.IdempotentEnum;
import com.hitsz.badboyChat.common.enums.ItemEnum;
import com.hitsz.badboyChat.common.event.UserRegisterEvent;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/3 21:02
 */
@Component
@Slf4j
public class UserRegisterListener {

    @Autowired
    private UserService userService;

    @Async
    @EventListener(classes = UserRegisterEvent.class)
    public void sendItem(UserRegisterEvent event){
        User user = event.getUser();
        userService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID,user.getId().toString());
    }
}

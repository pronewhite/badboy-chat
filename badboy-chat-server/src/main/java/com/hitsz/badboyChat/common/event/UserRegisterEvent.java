package com.hitsz.badboyChat.common.event;

import com.hitsz.badboyChat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/3 20:59
 */
@Getter
public class UserRegisterEvent extends ApplicationEvent {

    public User user;
    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}

package com.hitsz.badboyChat.common.event;

import com.hitsz.badboyChat.common.user.domain.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.test.context.event.ApplicationEvents;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/3 18:50
 */
@Getter
public class UserOnlineEvent extends ApplicationEvent {
    public User user;
    public UserOnlineEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}

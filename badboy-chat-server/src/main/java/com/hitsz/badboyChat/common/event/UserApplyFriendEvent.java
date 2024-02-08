package com.hitsz.badboyChat.common.event;

import com.hitsz.badboyChat.common.user.domain.entity.UserApply;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/8 12:47
 */
@Getter
public class UserApplyFriendEvent extends ApplicationEvent {

    public UserApply userApply;
    public UserApplyFriendEvent(Object source, UserApply userApply) {
        super(source);
        this.userApply = userApply;
    }
}

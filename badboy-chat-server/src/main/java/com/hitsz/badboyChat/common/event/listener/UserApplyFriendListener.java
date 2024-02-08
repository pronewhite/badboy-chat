package com.hitsz.badboyChat.common.event.listener;

import com.hitsz.badboyChat.common.event.UserApplyFriendEvent;
import com.hitsz.badboyChat.common.user.dao.UserApplyDao;
import com.hitsz.badboyChat.common.user.domain.entity.UserApply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/8 12:47
 */
@Component
@Slf4j
public class UserApplyFriendListener {

    @Autowired
    private UserApplyDao userApplyDao;

    @EventListener(classes = UserApplyFriendEvent.class)
    public void notifyFriend(UserApplyFriendEvent event) {
        UserApply userApply = event.getUserApply();
        // 提醒目标用户，有好友申请，此时可以一并将目标好友未读的好友申请返回给目标用户
        Integer unreadCount = userApplyDao.getUnreadCount(userApply.getTargetId());
        // todo 使用rocketMQ进行消息的推送
    }
}

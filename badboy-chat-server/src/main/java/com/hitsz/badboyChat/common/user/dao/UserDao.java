package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.enums.UserActiveStatusEnum;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/7 15:31
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

    public List<User> getFriendInfo(List<Long> friendUids) {
        return lambdaQuery()
                .in(User::getId, friendUids)
                .select(User::getId, User::getName, User::getAvatar, User::getActiveStatus)
                .list();
    }

    public Integer getOnlineMembers(List<Long> groupMembersUid) {
        return lambdaQuery()
                .eq(User::getActiveStatus, UserActiveStatusEnum.ONLINE.getStatus())
                .in(User::getId, groupMembersUid)
                .count();
    }

    public List<User> getMembers() {
        return lambdaQuery()
                .eq(User::getStatus, UserActiveStatusEnum.ONLINE.getStatus())
                .orderByDesc(User::getLastOptTime)
                .last("limit 100")
                .select(User::getId, User::getName, User::getAvatar)
                .list();
    }
}

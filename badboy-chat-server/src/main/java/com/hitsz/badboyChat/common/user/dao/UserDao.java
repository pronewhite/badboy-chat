package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.enums.UserActiveStatusEnum;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.mapper.UserMapper;
import com.hitsz.badboyChat.common.user.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    public CursorPageBaseResp<User> getCursorPage(List<Long> memberList, CursorPageBaseReq<Object> objectCursorPageBaseReq, UserActiveStatusEnum activeStatus) {
        return CursorUtils.getCursorPageByMysql(this, objectCursorPageBaseReq, wrapper -> {
            wrapper.eq(User::getStatus, activeStatus.getStatus());// 筛选上线或者离线的
            wrapper.eq(Objects.nonNull(memberList), User::getId, memberList); // 全员群和普通群有区别，全员群显示所有用户，普通群显示群成员
        }, User::getLastOptTime);
    }
}

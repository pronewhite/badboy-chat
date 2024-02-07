package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.domain.entity.UserFriend;
import com.hitsz.badboyChat.common.user.mapper.UserFriendMapper;
import com.hitsz.badboyChat.common.user.utils.CursorUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/7 15:23
 */
@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {


    public CursorPageBaseResp<UserFriend> getFriendPage(long uid, CursorPageBaseReq request) {
        return CursorUtils.getCursorPageByMysql(this, request, wrapper -> wrapper.eq(UserFriend::getUid, uid), UserFriend::getId);
    }
}

package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.dao.UserDao;
import com.hitsz.badboyChat.common.user.dao.UserFriendDao;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.domain.entity.UserFriend;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendResp;
import com.hitsz.badboyChat.common.user.mapper.UserFriendMapper;
import com.hitsz.badboyChat.common.user.service.UserFriendService;
import com.hitsz.badboyChat.common.user.service.adapter.FriendAdapter;
import com.hitsz.badboyChat.common.user.utils.CursorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author lenovo
* @description 针对表【user_friend(用户联系人表)】的数据库操作Service实现
* @createDate 2024-02-07 13:38:07
*/
@Service
public class UserFriendServiceImpl implements UserFriendService {

    @Autowired
    private UserFriendDao userFriendDao;
    @Autowired
    private UserDao userDao;

    @Override
    public CursorPageBaseResp<FriendResp> getFriendPage(long uid, CursorPageBaseReq request) {
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid,request);
        List<Long> friendUids = friendPage.getList().stream().map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
        List<User> friendInfo = userDao.getFriendInfo(friendUids);
        return CursorPageBaseResp.init(friendPage, FriendAdapter.buildFriendInfo(friendUids, friendInfo));
    }
}





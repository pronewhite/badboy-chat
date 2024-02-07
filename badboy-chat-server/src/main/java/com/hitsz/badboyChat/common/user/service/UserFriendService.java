package com.hitsz.badboyChat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.domain.entity.UserFriend;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendResp;

/**
* @author lenovo
* @description 针对表【user_friend(用户联系人表)】的数据库操作Service
* @createDate 2024-02-07 13:38:07
*/
public interface UserFriendService {

    /**
     *  获取好友分页信息
     * @param uid
     * @param request
     * @return
     */
    CursorPageBaseResp<FriendResp> getFriendPage(long uid, CursorPageBaseReq request);
}

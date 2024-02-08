package com.hitsz.badboyChat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.req.PageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.domain.vo.resp.PageBaseResp;
import com.hitsz.badboyChat.common.user.domain.entity.UserFriend;
import com.hitsz.badboyChat.common.user.domain.vo.req.DeleteFriendReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.FriendApplyApproveReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.FriendApplyReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendApplyResp;
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

    void applyFriend(long uid, FriendApplyReq friendApplyReq);

    void applyApprove(long uid, FriendApplyApproveReq friendApplyApproveReq);

    void delete(long uid, DeleteFriendReq deleteFriendReq);

    PageBaseResp<FriendApplyResp> getFriendApplyPage(long uid, PageBaseReq request);

    Integer getApplyUnread(long uid);
}

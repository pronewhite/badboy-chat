package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.annotation.RedissionLock;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.req.PageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.domain.vo.resp.PageBaseResp;
import com.hitsz.badboyChat.common.enums.ApplyStatusEnum;
import com.hitsz.badboyChat.common.event.UserApplyFriendEvent;
import com.hitsz.badboyChat.common.user.dao.UserApplyDao;
import com.hitsz.badboyChat.common.user.dao.UserDao;
import com.hitsz.badboyChat.common.user.dao.UserFriendDao;
import com.hitsz.badboyChat.common.user.domain.entity.RoomFriend;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.domain.entity.UserApply;
import com.hitsz.badboyChat.common.user.domain.entity.UserFriend;
import com.hitsz.badboyChat.common.user.domain.vo.req.DeleteFriendReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.FriendApplyApproveReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.FriendApplyReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendApplyResp;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendResp;
import com.hitsz.badboyChat.common.user.mapper.UserFriendMapper;
import com.hitsz.badboyChat.common.user.service.RoomService;
import com.hitsz.badboyChat.common.user.service.UserFriendService;
import com.hitsz.badboyChat.common.user.service.adapter.FriendAdapter;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import com.hitsz.badboyChat.common.user.utils.CursorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    @Autowired
    private UserApplyDao userApplyDao;
    @Autowired
    @Lazy
    private UserFriendService userFriendService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public CursorPageBaseResp<FriendResp> getFriendPage(long uid, CursorPageBaseReq request) {
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid,request);
        List<Long> friendUids = friendPage.getList().stream().map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
        List<User> friendInfo = userDao.getFriendInfo(friendUids);
        return CursorPageBaseResp.init(friendPage, FriendAdapter.buildFriendInfo(friendUids, friendInfo));
    }

    @Override
    @RedissionLock(key = "#uid")
    public void applyFriend(long uid, FriendApplyReq friendApplyReq) {
        // 判断当前用户是否已经与目标用户建立了好友关系
        UserFriend userFriend = userFriendDao.getFriendById(uid, friendApplyReq.getTargetUid());
        AssertUtil.isEmpty(userFriend, "你们已经是好友了");
        // 判断是否已经发起过申请
        UserApply userApply = userApplyDao.getFriendApply(uid, friendApplyReq.getTargetUid());
        AssertUtil.isEmpty(userApply, "你们已经申请过好友了");
        // 判断目标用户是否已经向自己申请过好友
        UserApply targetUserApply = userApplyDao.getFriendApply(friendApplyReq.getTargetUid(), uid);
        if(Objects.nonNull(targetUserApply)){
            userFriendService.applyApprove(uid, new FriendApplyApproveReq(targetUserApply.getId()));
            return;
        }
        // 申请入库
        UserApply insert = FriendAdapter.buildFriendApply(uid, friendApplyReq);
        userApplyDao.save(insert);
        // 发送申请的事件
        applicationEventPublisher.publishEvent(new UserApplyFriendEvent(this, insert));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissionLock(key = "#uid")
    public void applyApprove(long uid, FriendApplyApproveReq friendApplyApproveReq) {
        UserApply userApply = userApplyDao.getById(friendApplyApproveReq.getApplyId());
        AssertUtil.isEmpty(userApply, "申请不存在");
        // 判断是否已经处理过
        AssertUtil.equal(userApply.getStatus(),ApplyStatusEnum.WAIT_PROCESS.getCode(), "申请已经被处理过了");
        // 判断是否是目标用户
        if(userApply.getTargetId() != uid){
            return;
        }
        AssertUtil.equal(userApply.getTargetId(), uid, "这不是给你的好友申请哦！");
        // 同意好友申请
        userApplyDao.agreeApply(friendApplyApproveReq.getApplyId());
        // 创建好友关系
        createFriendRelation(uid, userApply.getTargetId());
        // 给对应的好友建立好友房间
        RoomFriend roomFriend = roomService.createRoomFriend(Arrays.asList(uid, userApply.getUid()));
        // todo 建立房间之后发送一条消息表示双方已经成功加上好友

    }

    @Override
    public void delete(long uid, DeleteFriendReq deleteFriendReq) {
        List<UserFriend> userFriends = userFriendDao.getUserFriends(uid, deleteFriendReq.getFriendId());
        AssertUtil.isNotEmpty(userFriends, "你们还不是好友");
        List<Long> userFriendsList = userFriends.stream().map(UserFriend::getId).collect(Collectors.toList());
        userFriendDao.removeByIds(userFriendsList);
        // 解散房间
        roomService.disableRoom(Arrays.asList(uid, deleteFriendReq.getFriendId()));
    }

    @Override
    public PageBaseResp<FriendApplyResp> getFriendApplyPage(long uid, PageBaseReq request) {
        IPage<UserApply> pageBaseResp = userApplyDao.getFriendApplyPage(uid, request);
        List<UserApply> records = pageBaseResp.getRecords();
        if(Objects.isNull(records)){
            return PageBaseResp.empty();
        }
        // 将查出的申请消息设置为已读
        replyApply(uid, records);
        return PageBaseResp.init(pageBaseResp, FriendAdapter.buildFriendApplyPage(records));
    }

    @Override
    public Integer getApplyUnread(long uid) {
        return userApplyDao.getUnreadCount(uid);
    }

    private void replyApply(long uid, List<UserApply> records) {
        List<Long> applyIds = records.stream().map(UserApply::getId).collect(Collectors.toList());
        userApplyDao.replyApply(uid, applyIds);
    }

    private void createFriendRelation(long uid, Long targetId) {
        UserFriend userFriend = new UserFriend();
        userFriend.setUid(uid);
        userFriend.setFriendUid(targetId);
        UserFriend userFriend2 = new UserFriend();
        userFriend2.setUid(targetId);
        userFriend2.setFriendUid(uid);
        userFriendDao.saveBatch(Arrays.asList(userFriend, userFriend2));
    }
}





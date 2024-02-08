package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.domain.vo.req.PageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.PageBaseResp;
import com.hitsz.badboyChat.common.enums.ApplyReadStatusEnum;
import com.hitsz.badboyChat.common.enums.ApplyStatusEnum;
import com.hitsz.badboyChat.common.enums.ApplyTypeEnum;
import com.hitsz.badboyChat.common.user.domain.entity.UserApply;
import com.hitsz.badboyChat.common.user.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/8 11:53
 */
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> {

    public UserApply getFriendApply(long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserApply::getUid, uid)
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getType, ApplyTypeEnum.FRIEND.getCode())
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_PROCESS.getCode())
                .one();
    }

    public Integer getUnreadCount(Long targetId) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, targetId)
                .eq(UserApply::getReadStatus, ApplyReadStatusEnum.NOT_READ.getCode())
                .count();
    }

    public void agreeApply(Long applyId) {
        lambdaUpdate()
                .set(UserApply::getStatus, ApplyStatusEnum.AGREE.getCode())
                .eq(UserApply::getId, applyId)
                .update();
    }

    public IPage<UserApply> getFriendApplyPage(long uid, PageBaseReq request) {
        return lambdaQuery()
                .eq(UserApply::getUid, uid)
                .eq(UserApply::getType, ApplyTypeEnum.FRIEND.getCode())
                .orderByDesc(UserApply::getCreateTime)
                .page(request.plusPage());

    }

    public void replyApply(long uid, List<Long> applyIds) {
        lambdaUpdate()
                .set(UserApply::getStatus, ApplyStatusEnum.AGREE.getCode())
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getReadStatus, ApplyReadStatusEnum.NOT_READ.getCode())
                .in(UserApply::getId, applyIds)
                .update();
    }
}

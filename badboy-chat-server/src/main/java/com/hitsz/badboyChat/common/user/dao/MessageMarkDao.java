package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.MessageMark;
import com.hitsz.badboyChat.common.user.mapper.MessageMarkMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 16:53
 */
@Service
public class MessageMarkDao extends ServiceImpl<MessageMarkMapper, MessageMark>{
    public List<MessageMark> getMessageMarkByIdsBatch(List<Long> collect) {
        return lambdaQuery()
                .in(MessageMark::getMsgId, collect)
                .eq(MessageMark::getStatus, YesOrNoEnum.NO.getCode())
                .list();
    }

    public MessageMark getMessageMark(Long uid, Long msgId) {
        return lambdaQuery()
                .eq(MessageMark::getUid, uid)
                .eq(MessageMark::getMsgId, msgId)
                .one();
    }

    public Integer getMsgMarkCount(Long msgId, Integer markType) {
        return lambdaQuery()
                .eq(MessageMark::getMsgId, msgId)
                .eq(MessageMark::getType, markType)
                .count();
    }
}

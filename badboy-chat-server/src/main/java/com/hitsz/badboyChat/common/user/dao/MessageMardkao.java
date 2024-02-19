package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class MessageMardkao extends ServiceImpl<MessageMarkMapper, MessageMark>{
    public List<MessageMark> getMessageMarkByIdsBatch(List<Long> collect) {
        return lambdaQuery()
                .in(MessageMark::getMsgId, collect)
                .list();
    }
}

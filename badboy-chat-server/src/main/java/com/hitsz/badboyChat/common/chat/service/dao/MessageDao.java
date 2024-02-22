package com.hitsz.badboyChat.common.chat.service.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.req.GetMessagePageReq;
import com.hitsz.badboyChat.common.chat.enums.MessageStatusEnum;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.mapper.MessageMapper;
import com.hitsz.badboyChat.common.user.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 11:24
 */
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message>{

    public CursorPageBaseResp<Message> getMsgCursorPage( GetMessagePageReq req, Long lastMsgId) {
        return CursorUtils.getCursorPageByMysql(this, req, wrapper -> {
            wrapper.eq(Message::getRoomId,req.getRoomId())
                    .eq(Message::getStatus, MessageStatusEnum.NORAML.getCode())
                    .le(Objects.nonNull(lastMsgId),Message::getId,lastMsgId);
        }, Message::getId);
    }

    public Integer getGapCount(Long roomId, Long id, Long replyId) {
        return lambdaQuery()
                .eq(Message::getRoomId,roomId)
                .gt(Message::getId,id)
                .le(Message::getId,replyId)
                .count();
    }
}

package com.hitsz.badboyChat.common.chat.service.strategy;

import com.hitsz.badboyChat.common.chat.domain.entity.msg.MessageExtra;
import com.hitsz.badboyChat.common.chat.domain.entity.msg.TextMsgDTO;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.msg.TextMsgResp;
import com.hitsz.badboyChat.common.chat.enums.MessageStatusEnum;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.chat.service.adapter.MessageAdapter;
import com.hitsz.badboyChat.common.chat.service.factory.MsgHandlerFactory;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.service.cache.MsgCache;
import com.hitsz.badboyChat.common.user.service.cache.UserInfoCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/17 15:25
 */
@Component
public class TextMsgHandler extends AbstractMsgHandler<TextMsgDTO>{

    @Autowired
    private MsgCache msgCache;
    @Autowired
    private UserInfoCache userInfoCache;
    @Override
    protected MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    protected void saveMsg(Message insert, TextMsgDTO body) {

    }

    @Override
    public Object showMsg(Message message) {
        TextMsgResp textMsgResp = new TextMsgResp();
        textMsgResp.setContent(message.getContent());
        textMsgResp.setUrlContentMap(Optional.ofNullable(message.getExtra()).map(MessageExtra::getUrlContentMap).orElse(null));
        textMsgResp.setReplyUidList(Optional.ofNullable(message.getExtra()).map(MessageExtra::getAtUidList).orElse(null));
        // 回复消息信息
        Optional<Message> replyMessage = Optional.ofNullable(message.getReplyMsgId()).map(msgCache::getMsg).filter(msg -> Objects.equals(msg.getStatus(), MessageStatusEnum.NORAML.getCode()));
        if(replyMessage.isPresent()){
            // 此消息是在回复另一条消息
            TextMsgResp.ReplyMsg replyMsgVO = new TextMsgResp.ReplyMsg();
            replyMsgVO.setId(replyMessage.get().getId());
            replyMsgVO.setUid(replyMsgVO.getUid());
            replyMsgVO.setType(replyMessage.get().getType());
            replyMsgVO.setBody(MsgHandlerFactory.getStrategyOrNull(replyMsgVO.getType()).showMsg(replyMessage.get()));
            // 查出回复用户的信息
            User user = userInfoCache.get(replyMessage.get().getFromUid());
            replyMsgVO.setUid(user.getId());
            replyMsgVO.setUserName(user.getName());
            // 设置回复的消息能不能跳转到,如果间隔消息的条数小于100那么可以跳转，否则不能跳转
            replyMsgVO.setCanCallback(YesOrNoEnum.getStatus(Objects.nonNull(message.getGapCount()) && message.getGapCount() <= MessageAdapter.CAN_CALLBACK));
            replyMsgVO.setGapCount(message.getGapCount());
            textMsgResp.setReply(replyMsgVO);
        }
        return textMsgResp;
    }
}

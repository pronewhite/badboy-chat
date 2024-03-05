package com.hitsz.badboyChat.common.chat.service.strategy;

import cn.hutool.core.collection.CollectionUtil;
import com.hitsz.badboyChat.common.chat.domain.dto.UrlInfo;
import com.hitsz.badboyChat.common.chat.domain.entity.msg.MessageExtra;
import com.hitsz.badboyChat.common.chat.domain.entity.msg.TextMsgDTO;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.msg.TextMsgResp;
import com.hitsz.badboyChat.common.chat.enums.MessageStatusEnum;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.chat.service.adapter.MessageAdapter;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.chat.service.factory.MsgHandlerFactory;
import com.hitsz.badboyChat.common.enums.RoleTypeEnum;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.service.RoleService;
import com.hitsz.badboyChat.common.user.service.cache.MsgCache;
import com.hitsz.badboyChat.common.user.service.cache.UserInfoCache;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import com.hitsz.badboyChat.common.utils.discover.PrioritizedUrlDiscover;
import com.hitsz.badboyChat.common.utils.sensitiveword.SensitiveWordBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/17 15:25
 */
@Component
public class TextMsgHandler extends AbstractMsgHandler<TextMsgDTO>{
    @Autowired
    private SensitiveWordBs sensitiveWordBs;
    @Autowired
    private MsgCache msgCache;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageDao messageDao;

    private static final PrioritizedUrlDiscover URL_DISCOVER = new PrioritizedUrlDiscover();


    @Override
    protected MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    protected void saveMsg(Message insert, TextMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(insert.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setExtra(extra);
        update.setContent(sensitiveWordBs.filter(body.getContent()));
        update.setId(insert.getId());
        // 如果是回复
        if(Objects.nonNull(body.getReplyId())){
            Integer gapCount = messageDao.getGapCount(insert.getRoomId(), insert.getId(), body.getReplyId());
            update.setGapCount(gapCount);
            update.setReplyMsgId(body.getReplyId());
        }
        // 如果消息中包含有链接，那么需要获取链接的标题，logo，描述信息
        Map<String, UrlInfo> contentMap = URL_DISCOVER.getContentMap(body.getContent());
        extra.setUrlContentMap(contentMap);
        // 艾特成员时需要保存艾特的成员id
        if(CollectionUtil.isNotEmpty(body.getReplyUids())){
            extra.setAtUidList(body.getReplyUids());
        }
        messageDao.updateById(update);
    }

    @Override
    protected void checkMsg(TextMsgDTO body, Long roomId, Long uid) {
        Long replyMsgId = body.getReplyId();
        // 如果回复的消息不是空，那么就需要组装回复的消息相关信息
        if (Objects.nonNull(replyMsgId)) {
            Message msg = msgCache.getMsg(replyMsgId);
            // 判断回复的消息是否存在
            AssertUtil.isNotEmpty(msg,"回复的消息不存在");
            // 判断回复的消息是否在当前房间
            AssertUtil.equal(msg.getRoomId(),roomId,"只能回复相同房间的消息");
        }
        if(CollectionUtil.isNotEmpty(body.getReplyUids())){
            // 前端传过来的uid可能会重复，所以需要去重
            List<Long> collect = body.getReplyUids().stream().distinct().collect(Collectors.toList());
            Map<Long, User> batch = userInfoCache.getBatch(collect);
            // 判断@的用户是否存在
            long count = batch.values().stream().filter(Objects::nonNull).count();
            AssertUtil.isTrue(count == collect.size(),"@用户不存在");
            //  判断是否@所有人
            // todo 为啥包括0L就是艾特全体成员
            boolean atAll = collect.contains(0L);
            if(atAll){
                AssertUtil.isTrue(roleService.hasPower(uid, RoleTypeEnum.CHAT_MANAGER),"无权限");
            }
        }
    }

    @Override
    protected Object showRespMsg(Message message) {
        return message.getContent();
    }

    @Override
    protected Object showReplyMsg(Message message) {
        return message.getContent();
    }

    @Override
    public Object showContactMsg(Message message) {
        return  message.getContent();
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
            replyMsgVO.setUid(replyMessage.get().getFromUid());
            replyMsgVO.setType(replyMessage.get().getType());
            replyMsgVO.setBody(MsgHandlerFactory.getStrategyOrNull(replyMsgVO.getType()).showRespMsg(replyMessage.get()));
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

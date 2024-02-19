package com.hitsz.badboyChat.common.chat.service.adapter;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
import com.hitsz.badboyChat.common.chat.enums.MessageStatusEnum;
import com.hitsz.badboyChat.common.chat.service.factory.MsgHandlerFactory;
import com.hitsz.badboyChat.common.chat.service.strategy.AbstractMsgHandler;
import com.hitsz.badboyChat.common.enums.MessageMarkTypeEnum;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.domain.entity.MessageMark;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 11:38
 */
public class MessageAdapter {

    public static final Integer MSG_CALLBACK_LIMIT = 2;
    public static Integer CAN_CALLBACK = 100;

    public static Message buildMessageInsert(ChatMessageReq req, Long uid) {
        return Message.builder()
                .fromUid(uid)
                .roomId(req.getRoomId())
                .type(req.getMsgType())
                .status(MessageStatusEnum.NORAML.getCode())
                .build();
    }

    public static List<ChatMessageResp> buildChatMessageResp(List<Message> messages, List<MessageMark> messageMarks, Long reciveUid) {
        // 先将messageMarks 转换为 map
        Map<Long, List<MessageMark>> messageMarkMap = messageMarks.stream().collect(Collectors.groupingBy(MessageMark::getMsgId));
        return messages.stream().map(message -> {
            ChatMessageResp chatMessageResp = new ChatMessageResp();
            chatMessageResp.setFromUser(buildFromUser(message.getFromUid()));
            chatMessageResp.setMessage(buildMessage(message,messageMarkMap.getOrDefault(message.getId(), new ArrayList<>()), reciveUid));
            return chatMessageResp;
        })
                // 默认按照发送时间升序排序，如果希望降序排序则可以写成.sorted(Comparator.comparing(a -> a.getMessage().getSendTime())).reversed()
                .sorted(Comparator.comparing(a -> a.getMessage().getSendTime()))
                .collect(Collectors.toList());
    }

    private static ChatMessageResp.Message buildMessage(Message message, List<MessageMark> messageMarks, Long reciveUid) {
        ChatMessageResp.Message messageResp = new ChatMessageResp.Message();
        messageResp.setId(message.getId());
        messageResp.setType(message.getType());
        messageResp.setRoomId(message.getRoomId());
        messageResp.setSendTime(message.getCreateTime());
        messageResp.setMessageMark(buildMessageMark(messageMarks, reciveUid));
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyOrNull(message.getType());
        if (Objects.nonNull(msgHandler)) {
            messageResp.setBody(msgHandler.showMsg(message));
        }
        return messageResp;
    }

    private static ChatMessageResp.MessageMark buildMessageMark(List<MessageMark> messageMarks, Long reciveUid) {
        // 按照消息标记类型进行分组
        Map<Integer, List<MessageMark>> messageMarkMap = messageMarks.stream().collect(Collectors.groupingBy(MessageMark::getType));
        List<MessageMark> messageMarkLikeList = messageMarkMap.get(MessageMarkTypeEnum.LIKE.getCode());
        List<MessageMark> messageMarkDisikeList = messageMarkMap.get(MessageMarkTypeEnum.DISLIKE.getCode());
        ChatMessageResp.MessageMark messageMark = new ChatMessageResp.MessageMark();
        messageMark.setDislikeCount(messageMarkDisikeList.size());
        messageMark.setLikeCount(messageMarkLikeList.size());
        messageMark.setUserLike(Optional.ofNullable(reciveUid).filter(uid -> messageMarkLikeList.stream().anyMatch(mark -> mark.getUid().equals(uid)))
                .map(a -> YesOrNoEnum.YES.getCode())
                .orElse(YesOrNoEnum.NO.getCode()));
        messageMark.setUserDislike(Optional.ofNullable(reciveUid).filter(uid -> messageMarkDisikeList.stream().anyMatch(mark -> mark.getUid().equals(uid)))
                .map(a -> YesOrNoEnum.YES.getCode())
                .orElse(YesOrNoEnum.NO.getCode()));
        return messageMark;
    }

    private static ChatMessageResp.UserInfo buildFromUser(Long fromUid) {
        ChatMessageResp.UserInfo userInfo = new ChatMessageResp.UserInfo();
        userInfo.setUid(fromUid);
        return userInfo;
    }
}

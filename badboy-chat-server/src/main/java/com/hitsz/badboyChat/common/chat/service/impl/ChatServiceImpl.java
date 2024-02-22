package com.hitsz.badboyChat.common.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.req.GetMessagePageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.req.MsgCallbackReq;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.chat.service.adapter.MessageAdapter;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.chat.service.strategy.RecallMsgHandler;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.enums.RoleTypeEnum;
import com.hitsz.badboyChat.common.event.MessageSendEvent;
import com.hitsz.badboyChat.common.chat.service.ChatService;
import com.hitsz.badboyChat.common.chat.service.dao.GroupMemBerDao;
import com.hitsz.badboyChat.common.chat.service.dao.RoomGroupDao;
import com.hitsz.badboyChat.common.chat.service.factory.MsgHandlerFactory;
import com.hitsz.badboyChat.common.chat.service.strategy.AbstractMsgHandler;
import com.hitsz.badboyChat.common.enums.CommonErrorEnum;
import com.hitsz.badboyChat.common.user.dao.ContactDao;
import com.hitsz.badboyChat.common.user.dao.MessageMardkao;
import com.hitsz.badboyChat.common.user.dao.RoomFriendDao;
import com.hitsz.badboyChat.common.user.domain.entity.*;
import com.hitsz.badboyChat.common.user.service.RoleService;
import com.hitsz.badboyChat.common.user.service.cache.RoomCache;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 12:34
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private RoomCache roomCache;
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private RoomGroupDao roomGroupDao;
    @Autowired
    private GroupMemBerDao groupMemBerDao;
    @Autowired
    private MessageMardkao messageMardkao;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RecallMsgHandler recallMsgHandler;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long chat(Long uid, ChatMessageReq req) {
        checkMsgValid(req, uid);
        AbstractMsgHandler msgHandler = MsgHandlerFactory.getStrategyOrNull(req.getMsgType());
        AssertUtil.isNotEmpty(msgHandler, CommonErrorEnum.PARAM_INVALID);
        Long msgId = msgHandler.checkAndSaveMsg(req, uid);
        // 推送发送消息的事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this,msgId));
        return msgId;
    }

    @Override
    public ChatMessageResp getChatResp(Message message, Long reciveUid) {
        return CollUtil.getFirst(getMsgRespBatch(Collections.singletonList(message), reciveUid));
    }

    @Override
    public CursorPageBaseResp<ChatMessageResp> getMsgPage(Long uid, GetMessagePageReq req) {
        // 通过最后一条消息id来限制被踢出群聊或者被删除好友的用户能看到的最后的一条消息
        Long lastMsgId = getLastMsgId(uid, req.getRoomId());
        // 游标分页获取消息列表
        CursorPageBaseResp<Message> msgCursorPage = messageDao.getMsgCursorPage(req, lastMsgId);
        // 如果msgCursorPage为空，则返回空
        if(CollectionUtil.isEmpty(msgCursorPage.getList())){
            return CursorPageBaseResp.empty();
        }
        return CursorPageBaseResp.init(msgCursorPage, getMsgRespBatch(msgCursorPage.getList(), uid));
    }

    @Override
    public void msgCallback(Long uid, MsgCallbackReq req) {
        Message message = messageDao.getById(req.getMsgId());
        checkCallBackMsgValid(message, uid);
        // 消息撤回
        recallMsgHandler.recallMsg(message, uid);
    }

    @Override
    public ChatMessageResp getMsgResp(Long msgId, Long uid) {
        Message message = messageDao.getById(msgId);
        return getChatResp(message, uid);
    }

    private void checkCallBackMsgValid(Message message, Long uid) {
        // 如果消息类型是撤回消息类型，那么不能撤回
        AssertUtil.notEqual(message.getType(), MessageTypeEnum.RECALL.getType(),"非法操作");
        // 判断当前用户是否有权限撤回消息
        AssertUtil.isNotEmpty(uid, "请先登录");
        boolean hasPower = roleService.hasPower(uid, RoleTypeEnum.CHAT_MANAGER);
        // 是管理员那么可以撤回任意消息
        if(hasPower){
            return;
        }
        // 不是管理员只能撤回自己发出的两分钟以内的消息
        // 判断要撤回的消息是否已经发出两分钟，如果两分钟以内那么可以直接撤回，否则不能撤回
        long between = DateUtil.between(message.getCreateTime(), DateUtil.date(), DateUnit.MINUTE);
        AssertUtil.isTrue(between <= MessageAdapter.MSG_CALLBACK_LIMIT,"消息已发出"+MessageAdapter.MSG_CALLBACK_LIMIT+"分钟，无法撤回");
    }

    private Long getLastMsgId(Long uid, Long roomId) {
        Room room = roomCache.get(roomId);
        AssertUtil.isNotEmpty(room, "房间不存在");
        // 如果是热点群聊，则直接返回null
        if(room.isHotRoom()){
            return null;
        }
        AssertUtil.isNotEmpty(uid, "请先登录");
        return contactDao.getLastMsgId(uid, roomId);
    }

    /**
     *  根据消息组装消息响应对象
     * @param messages 消息列表
     * @param reciveUid 接收者id（当前登录用户），主要用于判断当前用户是否对消息 进行了点赞、收藏、举报等操作
     * @return
     */
    private List<ChatMessageResp> getMsgRespBatch(List<Message> messages, Long reciveUid) {
        if(CollectionUtil.isEmpty(messages)){
            return new ArrayList<>();
        }
        // 取出所有消息的消息id，然后查出所有消息的标记信息
        List<MessageMark> messageMarks = messageMardkao.getMessageMarkByIdsBatch(messages.stream().map(Message::getId).collect(Collectors.toList()));
        return MessageAdapter.buildChatMessageResp(messages, messageMarks, reciveUid);
    }

    private void checkMsgValid(ChatMessageReq req, Long uid) {
        Room room = roomCache.get(req.getRoomId());
        // 如果是热点群聊直接跳过检验
        if (room.isHotRoom()) {
            return;
        }
        if (room.isRoomFriend()) {
            RoomFriend roomFriend = roomFriendDao.getByRoomId(req.getRoomId());
            AssertUtil.isNotEmpty(roomFriend, "你已经被删除好友了，无法发送消息哦");
        }
        if(room.isRoomGroup()){
            RoomGroup roomGroup = roomGroupDao.getRoomGroupById(req.getRoomId());
            GroupMember member = groupMemBerDao.getMember(roomGroup.getId(), uid);
            AssertUtil.isNotEmpty(member,"你已经被删除群聊了，无法发送消息哦");
        }
    }
}

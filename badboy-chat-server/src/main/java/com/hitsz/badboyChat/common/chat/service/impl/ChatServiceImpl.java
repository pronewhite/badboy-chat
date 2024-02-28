package com.hitsz.badboyChat.common.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.hitsz.badboyChat.common.annotation.RedissionLock;
import com.hitsz.badboyChat.common.chat.domain.vo.req.*;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMemberStatisticResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageReadResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.MsgReadInfoResp;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.chat.enums.MsgMarkActionTypeEnum;
import com.hitsz.badboyChat.common.chat.service.ChatMemberHelper;
import com.hitsz.badboyChat.common.chat.service.adapter.ChatAdapter;
import com.hitsz.badboyChat.common.chat.service.adapter.MessageAdapter;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.chat.service.factory.MsgMarkFactory;
import com.hitsz.badboyChat.common.chat.service.mark.AbstractMsgMarkStrategy;
import com.hitsz.badboyChat.common.chat.service.strategy.RecallMsgHandler;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.enums.RoleTypeEnum;
import com.hitsz.badboyChat.common.enums.UserActiveStatusEnum;
import com.hitsz.badboyChat.common.event.MessageSendEvent;
import com.hitsz.badboyChat.common.chat.service.ChatService;
import com.hitsz.badboyChat.common.chat.service.dao.GroupMemBerDao;
import com.hitsz.badboyChat.common.chat.service.dao.RoomGroupDao;
import com.hitsz.badboyChat.common.chat.service.factory.MsgHandlerFactory;
import com.hitsz.badboyChat.common.chat.service.strategy.AbstractMsgHandler;
import com.hitsz.badboyChat.common.enums.CommonErrorEnum;
import com.hitsz.badboyChat.common.exception.BusinessException;
import com.hitsz.badboyChat.common.user.dao.ContactDao;
import com.hitsz.badboyChat.common.user.dao.MessageMarkDao;
import com.hitsz.badboyChat.common.user.dao.RoomFriendDao;
import com.hitsz.badboyChat.common.user.dao.UserDao;
import com.hitsz.badboyChat.common.user.domain.entity.*;
import com.hitsz.badboyChat.common.user.service.ContactService;
import com.hitsz.badboyChat.common.user.service.RoleService;
import com.hitsz.badboyChat.common.user.service.cache.RoomCache;
import com.hitsz.badboyChat.common.user.service.cache.UserCache;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.ChatMemberResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
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
    private MessageMarkDao messageMarkDao;
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
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserCache userCache;
    @Autowired
    private UserDao userDao;

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


    /**
     * 消息标记
     * @param uid
     * @param req
     * 消息标记会有一致性的问题，因为会出现多个人一起标记同一条消息的行为，出现一致性的问题，因此需要加分布式锁
     */
    @Override
    @RedissionLock(key = "#uid")
    public void msgMark(Long uid, ChatMsgMarkReq req) {
        AbstractMsgMarkStrategy strategy = MsgMarkFactory.getStrategyOrNull(req.getMarkType());
        switch (MsgMarkActionTypeEnum.of(req.getActType())){
            case MARK:
                strategy.mark(uid, req.getMsgId());
                break;
            case CANCLE_MARK:
                strategy.unMark(uid, req.getMsgId());
                break;
            default:
                throw new BusinessException("该操作不支持");
        }
    }

    @Override
    public void msgRead(Long uid, ChatMsgReadReq req) {
        Contact contact = contactDao.getByRoomIdAndUid(req.getRoomId(), uid);
        if(Objects.nonNull(contact)){
            Contact update = new Contact();
            update.setId(contact.getId());
            update.setReadTime(new Date());
            update.setUid(uid);
            contactDao.updateById(update);
        }else{
            Contact insert = new Contact();
            insert.setRoomId(req.getRoomId());
            insert.setId(contact.getId());
            insert.setUid(uid);
            insert.setReadTime(new Date());
            contactDao.save(insert);
        }
    }

    @Override
    public Collection<MsgReadInfoResp> getMsgReadInfo(Long uid, MsgReadInfoReq req) {
        // 获取到需要展示已读未读数的消息
        List<Message> msgs = messageDao.getMsgs(uid, req.getMsgIds());
        msgs.forEach(msg -> AssertUtil.equal(uid, msg.getFromUid(), "只能查询自己发送的消息") );
        return contactService.getMsgReadInfo(uid, msgs);
    }

    @Override
    public CursorPageBaseResp<ChatMessageReadResp> getMsgReadPage(Long uid, ChatMsgReadInfoReq req) {
        Message message = messageDao.getById(req.getMsgId());
        AssertUtil.isNotEmpty(message, "消息不存在");
        AssertUtil.equal(uid, message.getFromUid(), "只能查询自己发送的消息");
        CursorPageBaseResp<Contact> page;
        if(req.getSearchType() == 1){
            // 未读
            page = contactDao.getUnReadPage(message, req);
        }else{
            page = contactDao.getReadPage(message, req);
        }
        return CursorPageBaseResp.init(page, MessageAdapter.buildMsgReadInfo(page.getList()));
    }

    @Override
    public ChatMemberStatisticResp getMemberStatistic() {
        ChatMemberStatisticResp resp = new  ChatMemberStatisticResp();
        Long onlineNumber = userCache.getOnlineNumber();
        resp.setOnlineNumber(onlineNumber.intValue());
        return  resp;
    }

    @Override
    public CursorPageBaseResp<ChatMemberResp> getMemberPage(MemberReq req, List<Long> memberList) {
        // 取出并解析游标
        Pair<UserActiveStatusEnum, String> cursor = ChatMemberHelper.getCursor(req.getCursor());
        UserActiveStatusEnum activeStatus = cursor.getFirst();
        String timeCursor = cursor.getSecond();
        List<ChatMemberResp> resultList = new ArrayList<>();
        CursorPageBaseResp<User> cursorPage;
        if(activeStatus == UserActiveStatusEnum.ONLINE){
            // 在线列表
            cursorPage = userDao.getCursorPage(memberList, new CursorPageBaseReq<>(req.getPageSize(), timeCursor), activeStatus);
            resultList.addAll(ChatAdapter.buildMemberListResp(cursorPage.getList()));
            if(cursorPage.getIsLast()){
                // 如果到了最后一页那么需要从离线列表中获取
                activeStatus = UserActiveStatusEnum.OFFLINE;
                Integer pageSize = req.getPageSize() - resultList.size();
                CursorPageBaseResp<User> offLinePage = userDao.getCursorPage(memberList, new CursorPageBaseReq<>(pageSize, timeCursor), activeStatus);
                resultList.addAll(ChatAdapter.buildMemberListResp(offLinePage.getList()));
            }
        }else{
            // 离线列表
            cursorPage = userDao.getCursorPage(memberList, new CursorPageBaseReq<>(req.getPageSize(), timeCursor), activeStatus);
            resultList.addAll(ChatAdapter.buildMemberListResp(cursorPage.getList()));
        }
        // 获取群成员角色
        List<Long> uidList = resultList.stream().map(ChatMemberResp::getUid).collect(Collectors.toList());
        RoomGroup roomGroup = roomGroupDao.getRoomGroupByRoomId(req.getRoomId());
        List<GroupMember> members = groupMemBerDao.getMembers(roomGroup.getId(), uidList);
        Map<Long, GroupMember> memberMap = members.stream().collect(Collectors.toMap(GroupMember::getUid, Function.identity()));
        resultList.forEach(member -> {
            member.setRoleId(memberMap.get(member.getUid()).getRole());
        });
        return new CursorPageBaseResp<>(ChatMemberHelper.generateCursor(cursorPage.getCursor(), activeStatus),cursorPage.getIsLast(), resultList);
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
        List<MessageMark> messageMarks = messageMarkDao.getMessageMarkByIdsBatch(messages.stream().map(Message::getId).collect(Collectors.toList()));
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
            RoomGroup roomGroup = roomGroupDao.getRoomGroupByRoomId(req.getRoomId());
            GroupMember member = groupMemBerDao.getMember(roomGroup.getId(), uid);
            AssertUtil.isNotEmpty(member,"你已经被删除群聊了，无法发送消息哦");
        }
    }
}

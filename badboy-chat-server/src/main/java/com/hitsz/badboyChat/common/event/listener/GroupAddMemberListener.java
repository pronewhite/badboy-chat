package com.hitsz.badboyChat.common.event.listener;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.service.ChatService;
import com.hitsz.badboyChat.common.chat.service.adapter.ChatAdapter;
import com.hitsz.badboyChat.common.event.GroupAddMemberEvent;
import com.hitsz.badboyChat.common.user.dao.UserDao;
import com.hitsz.badboyChat.common.user.domain.entity.GroupMember;
import com.hitsz.badboyChat.common.user.domain.entity.RoomGroup;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.service.cache.GroupMemberCache;
import com.hitsz.badboyChat.common.user.service.cache.UserInfoCache;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBaseResp;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSMemberChange;
import com.hitsz.badboyChat.common.websocket.service.PushService;
import com.hitsz.badboyChat.common.websocket.service.adapter.WebSocketAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/28 13:54
 */
@Component
public class GroupAddMemberListener {

    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private PushService pushService;


    @Async
    @TransactionalEventListener(fallbackExecution = true, classes = GroupAddMemberEvent.class)
    public void sendAddMsg(GroupAddMemberEvent event){// 发送一条系统消息
        List<GroupMember> members = event.getMembers();
        List<Long> membersList = members.stream().map(GroupMember::getUid).collect(Collectors.toList());
        Long inviteUid = event.getUid();
        User user = userInfoCache.get(inviteUid);
        RoomGroup roomGroup = event.getRoomGroup();
        ChatMessageReq chatMessageReq = ChatAdapter.buildAddUserMsg(roomGroup.getRoomId(), user, userInfoCache.getBatch(membersList));
        chatService.chat(inviteUid, chatMessageReq);
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true,classes = GroupAddMemberEvent.class)
    public void sendChangePush(GroupAddMemberEvent event){// 通知群里的成员有新成员加入
        RoomGroup roomGroup = event.getRoomGroup();
        List<GroupMember> members = event.getMembers();
        // 邀请的用户uid
        List<Long> uids = members.stream().map(GroupMember::getUid).collect(Collectors.toList());
        List<User> users = userDao.listByIds(uids);
        // 获取群聊中的群成员id
        List<GroupMember> memberList = groupMemberCache.getMemberList(roomGroup.getId());
        List<Long> toPushUids = memberList.stream().map(GroupMember::getUid).collect(Collectors.toList());
        users.forEach(user -> {
            WSBaseResp<WSMemberChange> ws = WebSocketAdapter.buildAddUserPush(user, roomGroup.getRoomId());
            pushService.sendPushMsg(ws, toPushUids);
        });
        // 移除缓存
        groupMemberCache.evictMemberUidList(roomGroup.getId());
    }
}

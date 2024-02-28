package com.hitsz.badboyChat.common.chat.service.adapter;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.GroupMemberListResp;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.chat.enums.UserRoomRoleEnum;
import com.hitsz.badboyChat.common.user.domain.entity.GroupMember;
import com.hitsz.badboyChat.common.user.domain.entity.RoomFriend;
import com.hitsz.badboyChat.common.user.domain.entity.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/26 22:47
 */
public class ChatAdapter {
    public static List<GroupMemberListResp> buildGroupMemberListResp(List<User> users) {
        return users.stream().map(user -> {
            GroupMemberListResp resp = new GroupMemberListResp();
            resp.setUid(user.getId());
            resp.setName(user.getName());
            resp.setAvatar(user.getAvatar());
            return  resp;
        }).collect(Collectors.toList());
    }

    public static List<GroupMemberListResp> buildGroupMemberListResp(Map<Long, User> userMap) {
        return buildGroupMemberListResp(new ArrayList<>(userMap.values()));
    }

    public static Set<Long> getFriendUidSet(Collection<RoomFriend> values, Long uid) {
        return values.stream()
                .map(a -> getFriendUid(a, uid))
                .collect(Collectors.toSet());
    }

    public static Long getFriendUid(RoomFriend roomFriend, Long uid) {
        return roomFriend.getUid1() == uid ? roomFriend.getUid2() : roomFriend.getUid1();
    }

    public static List<GroupMember> buildAddUserResp(List<Long> toAddUids, Long groupId) {
        return toAddUids.stream().map(uid -> {
            GroupMember groupMember = new GroupMember();
            groupMember.setUid(uid);
            groupMember.setGroupId(groupId);
            groupMember.setRole(UserRoomRoleEnum.USER.getCode());
            return groupMember;
        }).collect(Collectors.toList());
    }

    public static ChatMessageReq buildAddUserMsg(Long roomId, User user, Map<Long, User> toAddUserMap) {
        ChatMessageReq chatMessageReq = new  ChatMessageReq();
        chatMessageReq.setRoomId(roomId);
        chatMessageReq.setMsgType(MessageTypeEnum.SYSTEM.getType());
        StringBuilder sb = new StringBuilder();
        sb.append(user.getName())
                .append("\"")
                .append("邀请了")
                .append(toAddUserMap.values().stream().map(u -> "\"" + user.getName() + "\"").collect(Collectors.joining(",")));
        chatMessageReq.setMsgContent(sb.toString());
        return chatMessageReq;
    }
}

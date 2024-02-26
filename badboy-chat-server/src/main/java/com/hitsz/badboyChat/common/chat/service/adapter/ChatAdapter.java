package com.hitsz.badboyChat.common.chat.service.adapter;

import com.hitsz.badboyChat.common.chat.domain.vo.resp.GroupMemberListResp;
import com.hitsz.badboyChat.common.user.domain.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
}

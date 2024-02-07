package com.hitsz.badboyChat.common.user.service.adapter;

import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendResp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/7 15:37
 */
public class FriendAdapter {


    public static List<FriendResp> buildFriendInfo(List<Long> friendUids, List<User> friendInfo) {
        // 将friendInfo按照用户id组装成map
        Map<Long, User> userMap = friendInfo.stream().collect(Collectors.toMap(User::getId, user -> user));
        // 遍历friendUids
        return friendUids.stream().map(friendUid -> {
            FriendResp friendResp = new FriendResp();
            friendResp.setUid(friendUid);
            User user = userMap.get(friendUid);
            friendResp.setUsername(user.getName());
            friendResp.setActiveStatus(user.getActiveStatus());
            friendResp.setAvatar(user.getAvatar());
            return friendResp;
        }).collect(Collectors.toList());
    }
}

package com.hitsz.badboyChat.common.user.service.adapter;

import com.hitsz.badboyChat.common.enums.*;
import com.hitsz.badboyChat.common.user.domain.entity.Room;
import com.hitsz.badboyChat.common.user.domain.entity.RoomFriend;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.domain.entity.UserApply;
import com.hitsz.badboyChat.common.user.domain.vo.req.FriendApplyReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendApplyResp;
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

    public static UserApply buildFriendApply(long uid, FriendApplyReq friendApplyReq) {
        return UserApply.builder()
                .uid(uid)
                .targetId(friendApplyReq.getTargetUid())
                .msg(friendApplyReq.getMsg())
                .status(ApplyStatusEnum.WAIT_PROCESS.getCode())
                .type(ApplyTypeEnum.FRIEND.getCode())
                .readStatus(ApplyReadStatusEnum.NOT_READ.getCode())
                .build();
    }

    public static String generateRoomKey(List<Long> roomFriendsUids) {
        return roomFriendsUids.stream().sorted().map(String::valueOf).collect(Collectors.joining("_"));
    }

    public static Room buildInserRoom() {
        return Room.builder()
                .type(RoomTypeEnum.SINGLE_CHAT.getCode())
                .hotFlag(RoomHotFlagEnum.NO.getCode())
                .build();
    }

    public static RoomFriend buildRoomFriend(Long id, List<Long> roomFriendsUids,String roomKey) {
        return RoomFriend.builder()
                .roomId(id)
                .roomKey(roomKey)
                .uid1(roomFriendsUids.get(0))
                .uid2(roomFriendsUids.get(1))
                .status(RoomStatusEnum.NORMAL.getCode())
                .build();
    }

    public static List<FriendApplyResp> buildFriendApplyPage(List<UserApply> records) {
        return records.stream().map(userApply -> {
            FriendApplyResp friendApplyResp = new FriendApplyResp();
            friendApplyResp.setApplyId(userApply.getId());
            friendApplyResp.setType(userApply.getType());
            friendApplyResp.setMsg(userApply.getMsg());
            friendApplyResp.setStatus(userApply.getStatus());
            friendApplyResp.setUid(userApply.getUid());
            return friendApplyResp;
        }).collect(Collectors.toList());
    }
}

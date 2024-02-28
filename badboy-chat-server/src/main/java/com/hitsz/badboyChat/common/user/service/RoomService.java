package com.hitsz.badboyChat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hitsz.badboyChat.common.chat.domain.vo.req.AddUserReq;
import com.hitsz.badboyChat.common.chat.domain.vo.req.RemoveUserReq;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.GroupMemberListResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.RoomDetailResp;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.user.domain.entity.Room;
import com.hitsz.badboyChat.common.user.domain.entity.RoomFriend;

import java.util.List;

/**
* @author lenovo
* @description 针对表【room(房间表)】的数据库操作Service
* @createDate 2024-02-07 13:26:12
*/
public interface RoomService {

    RoomFriend createRoomFriend(List<Long> roomFriendsUids);

    void disableRoom(List<Long> list);

    RoomDetailResp getRoomDetail(Long id, Long uid);

    List<GroupMemberListResp> getMemberList(Long roomId);

    void removeUser(RemoveUserReq req, Long uid);

    void addUser(AddUserReq req, Long uid);
}

package com.hitsz.badboyChat.common.chat.controller;

import com.hitsz.badboyChat.common.chat.domain.vo.req.*;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.GroupMemberListResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.RoomDetailResp;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.service.RoomService;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.ChatMemberResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/26 16:05
 */
@RestController
@RequestMapping("/capi/room")
@Api(tags = "房间相关接口")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @ApiOperation("获取房间详情")
    @PostMapping("/public/detail")
    public ApiResult<RoomDetailResp> getRoomDetail(@Valid @RequestBody IdReqVO req) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(roomService.getRoomDetail(req.getId(), uid));
    }

    @ApiOperation("群成员列表")
    @PostMapping("/public/memberpage")
    public ApiResult<CursorPageBaseResp<ChatMemberResp>> getMemberList(@Valid @RequestBody MemberReq req) {
        return ApiResult.success(roomService.getMemberPage(req));
    }

    @ApiOperation("获取群成员列表,@专用")
    @PostMapping("/public/memberList")
    public ApiResult<List<GroupMemberListResp>> getGroupMemberList(@Valid @RequestBody IdReqVO req) {
        return ApiResult.success(roomService.getMemberList(req.getId()));
    }

    @ApiOperation("移除群成员")
    @PostMapping("/remove/user")
    public ApiResult<Void> removeUser(@Valid @RequestBody RemoveUserReq req){
        Long uid = RequestHolder.get().getUid();
        roomService.removeUser(req, uid);
        return ApiResult.success();
    }

    @ApiOperation("邀请好友")
    @PostMapping("/add/user")
    public ApiResult<Void> addUser(@Valid @RequestBody AddUserReq req){
        Long uid =  RequestHolder.get().getUid();
        roomService.addUser(req, uid);
        return ApiResult.success();
    }

    @ApiOperation("退出群聊")
    @PostMapping("/exit/group")
    public ApiResult<Void> exitGroup(@RequestBody IdReqVO req){
        Long uid = RequestHolder.get().getUid();
        roomService.exitGroup(req.getId(), uid);
        return ApiResult.success();
    }

    @ApiOperation("创建群聊")
    @PostMapping("/add/group")
    public ApiResult<IdReqVO> addGroup(@RequestBody GroupAddReq req){
        Long uid = RequestHolder.get().getUid();
        return  ApiResult.success(roomService.addGroup(req, uid));
    }

    @ApiOperation("添加管理员")
    @PostMapping("/add/admin")
    public ApiResult<Void> addAdmin(@Valid @RequestBody AdminAddOrRemoveReq req){
        Long uid = RequestHolder.get().getUid();
        roomService.addAdmin(req, uid);
        return ApiResult.success();
    }

    @ApiOperation("移除管理员")
    @PostMapping("/remove/admin")
    public ApiResult<Void> removeAdmin(@Valid @RequestBody AdminAddOrRemoveReq req){
        Long uid = RequestHolder.get().getUid();
        roomService.removeAdmin(req, uid);
        return ApiResult.success();
    }

}

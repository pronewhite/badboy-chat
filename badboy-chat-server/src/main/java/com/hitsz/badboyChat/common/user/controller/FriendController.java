package com.hitsz.badboyChat.common.user.controller;

import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.req.PageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.domain.vo.resp.PageBaseResp;
import com.hitsz.badboyChat.common.user.domain.vo.req.DeleteFriendReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.FriendApplyApproveReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.FriendApplyReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendApplyResp;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendResp;
import com.hitsz.badboyChat.common.user.service.UserFriendService;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/7 13:20
 */
@RestController
@RequestMapping("/capi/user/friend")
@Api("好友相关接口")
public class FriendController {

    @Autowired
    private UserFriendService userFriendService;

    @GetMapping("/getFriendPage")
    @ApiOperation("获取好友列表")
    public ApiResult<CursorPageBaseResp<FriendResp>> getFriendPage(@Valid CursorPageBaseReq request){
        // 拿出uid
        long uid = RequestHolder.get().getUid();
        return ApiResult.success(userFriendService.getFriendPage(uid, request));
    }

    @PostMapping("/apply/friend")
    @ApiOperation("发起好友申请")
    public ApiResult<Void> applyFriend(@Valid @RequestBody FriendApplyReq friendApplyReq){
        // 拿出uid
        long uid = RequestHolder.get().getUid();
        userFriendService.applyFriend(uid, friendApplyReq);
        return ApiResult.success();
    }

    @PostMapping("/apply/approve")
    @ApiOperation("审批好友申请")
    public ApiResult<Void> applyApprove(@Valid @RequestBody FriendApplyApproveReq friendApplyApproveReq){
        // 拿出uid
        long uid = RequestHolder.get().getUid();
        userFriendService.applyApprove(uid, friendApplyApproveReq);
        return ApiResult.success();
    }

    @DeleteMapping("/delete/friend")
    @ApiOperation("删除好友")
    public ApiResult<Void> delete(@Valid @RequestBody DeleteFriendReq deleteFriendReq){
        // 拿出uid
        long uid = RequestHolder.get().getUid();
        userFriendService.delete(uid, deleteFriendReq);
        return ApiResult.success();
    }

    @GetMapping("/apply/page")
    @ApiModelProperty("获取好友申请列表")
    public ApiResult<PageBaseResp<FriendApplyResp>> getFriendApplyPage(@Valid PageBaseReq request){
        long uid = RequestHolder.get().getUid();
        return ApiResult.success(userFriendService.getFriendApplyPage(uid, request));
    }

    @GetMapping("/apply/unread")
    @ApiModelProperty("申请未读数")
    public ApiResult<Integer> getApplyUnread(){
        long uid = RequestHolder.get().getUid();
        return ApiResult.success(userFriendService.getApplyUnread(uid));
    }
}

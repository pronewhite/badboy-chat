package com.hitsz.badboyChat.common.user.controller;

import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.domain.vo.resp.FriendResp;
import com.hitsz.badboyChat.common.user.service.UserFriendService;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/getFriendPage")
    @ApiOperation("获取好友列表")
    public ApiResult<CursorPageBaseResp<FriendResp>> getFriendPage(@Valid @RequestBody CursorPageBaseReq request){
        // 拿出uid
        long uid = RequestHolder.get().getUid();
        return ApiResult.success(userFriendService.getFriendPage(uid, request));
    }
}

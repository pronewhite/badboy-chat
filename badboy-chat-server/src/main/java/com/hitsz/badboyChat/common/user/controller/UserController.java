package com.hitsz.badboyChat.common.user.controller;

import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.enums.RoleTypeEnum;
import com.hitsz.badboyChat.common.user.domain.vo.req.BlackUserReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.ModifyName;
import com.hitsz.badboyChat.common.user.domain.vo.req.SummeryInfoReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.WearBadgeReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.BadgeResp;
import com.hitsz.badboyChat.common.user.domain.vo.resp.SummeryInfoResp;
import com.hitsz.badboyChat.common.user.domain.vo.resp.UserInfoResp;
import com.hitsz.badboyChat.common.user.service.RoleService;
import com.hitsz.badboyChat.common.user.service.UserService;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 16:16
 */
@RestController
@RequestMapping("/capi/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/public/userInfo")
    @ApiOperation(value = "获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo(){
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(userService.getUserInfo(uid));
    }

    @GetMapping("/modifyName")
    @ApiOperation(value = "修改名称")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyName modifyName){
        Long uid = RequestHolder.get().getUid();
        userService.modifyName(uid,modifyName.getName());
        return ApiResult.success();
    }

    @GetMapping("/badges")
    @ApiOperation(value = "获取徽章列表")
    public ApiResult<List<BadgeResp>> getBadges(){
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(userService.getBadges(uid));
    }

    @GetMapping("/wearing/badge")
    @ApiOperation(value = "佩戴徽章")
    public ApiResult<Void> wearingBadge(@Valid @RequestBody WearBadgeReq wearBadgeReq){
        Long uid = RequestHolder.get().getUid();
        userService.wearBadge(uid,wearBadgeReq);
        return ApiResult.success();
    }

    @PutMapping("/black")
    @ApiOperation(value = "拉黑用户")
    public ApiResult<Void> blackUser(@Valid @RequestBody BlackUserReq blackUserReq){
        // 取出用户id
        Long uid = RequestHolder.get().getUid();
        boolean hasPower = roleService.hasPower(uid, RoleTypeEnum.ADMIN);
        AssertUtil.isTrue(hasPower,"权限不足");
        userService.blackUser(blackUserReq);
        return ApiResult.success();
    }

    @PostMapping("/info")
    @ApiOperation(value = "获取用户信息")
    public ApiResult<List<SummeryInfoResp>> getUserInfo(@Valid @RequestBody SummeryInfoReq req){
        return ApiResult.success(userService.getSummeryInfo(req));
    }

}

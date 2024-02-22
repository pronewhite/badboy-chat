package com.hitsz.badboyChat.common.user.controller;

import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.user.domain.vo.req.EmojiAddReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.EmojiDelReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.EmojiResp;
import com.hitsz.badboyChat.common.user.service.UserEmojiService;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/21 13:31
 */
@RestController
@RequestMapping("/capi/user/emoji")
@Api(tags = "用户表情包接口")
public class UserEmojiController {

    @Autowired
    private UserEmojiService userEmojiService;

    @GetMapping("/list")
    @ApiOperation("获取用户表情包列表")
    public ApiResult<List<EmojiResp>> getUserEmoji(){
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(userEmojiService.getUserEmojiList(uid));
    }

    @PostMapping("/add")
    @ApiOperation("添加表情包")
    public ApiResult<Void> addEmoji(@Valid @RequestBody EmojiAddReq req){
        Long uid =  RequestHolder.get().getUid();
        userEmojiService.addEmoji(uid,req);
        return ApiResult.success();
    }

    @PostMapping("/delete")
    public ApiResult<Void> delEmoji(@Valid @RequestBody EmojiDelReq req){
        Long uid = RequestHolder.get().getUid();
        userEmojiService.delEmoji(uid,req);
        return ApiResult.success();
    }

}

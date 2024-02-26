package com.hitsz.badboyChat.common.chat.controller;

import com.hitsz.badboyChat.common.chat.domain.vo.req.*;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageReadResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.MsgReadInfoResp;
import com.hitsz.badboyChat.common.chat.service.ChatService;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.enums.BlackTypeEnum;
import com.hitsz.badboyChat.common.user.service.cache.UserCache;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/12 21:22
 */
@RestController
@RequestMapping("/cpi/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private UserCache userCache;

    @ApiOperation("发送消息")
    @PostMapping("/send/msg")
    public ApiResult<ChatMessageResp> chat(@Valid @RequestBody ChatMessageReq req){
        Long uid = RequestHolder.get().getUid();
        Long msgId = chatService.chat(uid, req);
        return ApiResult.success(chatService.getMsgResp(msgId, uid));
    }

    @ApiOperation("获取消息列表")
    @GetMapping("/getMsg/page")
    public ApiResult<CursorPageBaseResp<ChatMessageResp>> getMsgPage(@Valid GetMessagePageReq req){
        Long uid = RequestHolder.get().getUid();
        CursorPageBaseResp<ChatMessageResp> msgPage = chatService.getMsgPage(uid, req);
        // 过滤掉被拉黑用户的消息
        filterBlackUserMsg(msgPage);
        return ApiResult.success(msgPage);
    }

    private void filterBlackUserMsg(CursorPageBaseResp<ChatMessageResp> msgPage) {
        Set<String> blackUserSet = getBlackUserSet();
        msgPage.getList().removeIf(a -> blackUserSet.contains(a.getFromUser().getUid().toString()));
    }

    private Set<String> getBlackUserSet() {
        return userCache.getBlackMap().getOrDefault(BlackTypeEnum.UID.getCode(), new HashSet<>());
    }

    @ApiOperation("撤回消息")
    @PostMapping("/msg/callback")
    public ApiResult<Void> msgCallback(@Valid @RequestBody MsgCallbackReq req){
        Long uid = RequestHolder.get().getUid();
        chatService.msgCallback(uid, req);
        return ApiResult.success();
    }

    @PutMapping("/msg/mark")
    @ApiOperation("消息标记")
    public ApiResult<Void> msgMark(@Valid @RequestBody ChatMsgMarkReq req){
        chatService.msgMark(RequestHolder.get().getUid(), req);
        return ApiResult.success();
    }

    @ApiOperation("获取消息已读未读列表")
    @PostMapping("/msg/read/page")
    public ApiResult<CursorPageBaseResp<ChatMessageReadResp>> getMsgReadPage(@Valid @RequestBody ChatMsgReadInfoReq req){
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(chatService.getMsgReadPage(uid, req));
    }

    @PostMapping("/msg/read/info")
    @ApiOperation("获取消息已读信息")
    public ApiResult<Collection<MsgReadInfoResp>> getMsgReadInfo(@Valid @RequestBody MsgReadInfoReq req){
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(chatService.getMsgReadInfo(uid, req));
    }

    @PostMapping("/msg/read")
    @ApiOperation("消息已读")
    public ApiResult<Void> msgRead(@Valid @RequestBody ChatMsgReadReq req){
        Long uid = RequestHolder.get().getUid();
        chatService.msgRead(uid, req);
        return  ApiResult.success();
    }

}

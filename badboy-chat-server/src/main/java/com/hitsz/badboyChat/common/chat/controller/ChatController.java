package com.hitsz.badboyChat.common.chat.controller;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.req.GetMessagePageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.req.MsgCallbackReq;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
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

}

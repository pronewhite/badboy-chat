package com.hitsz.badboyChat.common.chat.controller;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
import com.hitsz.badboyChat.common.chat.service.ChatService;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @PostMapping("/send/msg")
    public ApiResult<ChatMessageResp> chat(@Valid @RequestBody ChatMessageReq req){
        Long uid = RequestHolder.get().getUid();
        chatService.chat(uid, req);

        return ApiResult.success();
    }

}

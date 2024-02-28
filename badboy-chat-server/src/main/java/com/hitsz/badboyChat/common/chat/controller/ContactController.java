package com.hitsz.badboyChat.common.chat.controller;

import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatRoomResp;
import com.hitsz.badboyChat.common.chat.domain.vo.req.IdReqVO;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.service.ContactService;
import com.hitsz.badboyChat.common.user.service.RoomService;
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
 * Create by 2024/2/26 13:13
 */
@RestController
@RequestMapping("/contact/chat")
@Api(tags = "会话接口")
public class ContactController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private RoomService roomService;

    @ApiOperation("获取聊天室列表")
    @PostMapping("/public/contact/page")
    public ApiResult<CursorPageBaseResp<ChatRoomResp>> getChatRooms(@Valid @RequestBody CursorPageBaseReq req){
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(contactService.getChatRooms(uid, req));
    }

    @ApiOperation("获取聊天室详情")
    @PostMapping("/public/contact/detail")
    public ApiResult<ChatRoomResp> getCharRoomDetail(@Valid IdReqVO req){
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(contactService.getContactDetail(req.getId(), uid));
    }

    @ApiOperation("获取聊天室详情(好友发消息使用)")
    @PostMapping("/public/contact/detail/friend")
    public ApiResult<ChatRoomResp> getCharRoomDetailFriend(@Valid IdReqVO req){
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(contactService.getContactDetailFriend(req.getId(), uid));
    }
}

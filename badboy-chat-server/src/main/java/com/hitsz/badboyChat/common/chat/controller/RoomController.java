package com.hitsz.badboyChat.common.chat.controller;

import com.hitsz.badboyChat.common.chat.domain.vo.req.IdReqVO;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.GroupMemberListResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.RoomDetailResp;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.user.service.RoomService;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import io.swagger.annotations.ApiModelProperty;
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
public class RoomController {

    @Autowired
    private RoomService roomService;

    @ApiOperation("获取房间详情")
    @PostMapping("/public/detail")
    public ApiResult<RoomDetailResp> getRoomDetail(@Valid @RequestBody IdReqVO req) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(roomService.getRoomDetail(req.getId(), uid));
    }

    @ApiOperation("获取群成员列表,@专用")
    @PostMapping("/public/memberList")
    public ApiResult<List<GroupMemberListResp>> getGroupMemberList(@Valid @RequestBody IdReqVO req) {
        return ApiResult.success(roomService.getMemberList(req.getId()));
    }

}

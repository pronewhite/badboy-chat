package com.hitsz.badboyChat.common.chat.domain.vo.req;

import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/26 18:24
 */
@Data
public class ChatMsgReadInfoReq extends CursorPageBaseReq {

    @ApiModelProperty("消息id")
    private Long msgId;

    @ApiModelProperty("查询类型1.查询未读 2.查询已读")
    private Integer searchType;

}

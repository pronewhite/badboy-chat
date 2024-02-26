package com.hitsz.badboyChat.common.chat.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/26 16:14
 */
@Data
public class ChatMsgReadReq {

    @ApiModelProperty("房间id")
    private Long roomId;

}

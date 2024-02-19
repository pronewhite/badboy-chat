package com.hitsz.badboyChat.common.chat.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/17 23:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsgCallbackReq {
    @ApiModelProperty("要撤回的消息id")
    private Long msgId;
    @ApiModelProperty("撤回消息的房间id")
    private Long roomId;
}

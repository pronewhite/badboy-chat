package com.hitsz.badboyChat.common.chat.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/18 0:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMsgRecallDTO {
    @ApiModelProperty("撤回的消息id")
    private Long msgId;
    @ApiModelProperty("撤回消息的房间id")
    private Long roomId;
    @ApiModelProperty("撤回消息的用户id")
    private Long recallId;
}

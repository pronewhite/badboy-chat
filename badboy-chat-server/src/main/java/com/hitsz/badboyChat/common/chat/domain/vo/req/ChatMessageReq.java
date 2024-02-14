package com.hitsz.badboyChat.common.chat.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/12 21:25
 */
@Data
@AllArgsConstructor
@Builder
public class ChatMessageReq {
    @NonNull
    @ApiModelProperty("房间id")
    private Long roomId;
    @NonNull
    @ApiModelProperty("消息类型")
    private Integer msgType;
    @ApiModelProperty("消息内容")
    private Object msgContent;
}

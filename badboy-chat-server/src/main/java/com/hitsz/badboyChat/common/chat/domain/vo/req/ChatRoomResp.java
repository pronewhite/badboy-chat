package com.hitsz.badboyChat.common.chat.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/26 13:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResp {
    @ApiModelProperty("房间id")
    private Long roomId;
    @ApiModelProperty("房间类型")
    private Integer type;
    @ApiModelProperty("是否是热点群聊")
    private Integer hotFlag;
    @ApiModelProperty("群聊名称")
    private String name;
    @ApiModelProperty("群聊头像")
    private String avatar;
    @ApiModelProperty("最新消息")
    private String text;
    @ApiModelProperty("最新消息发送时间")
    private Date activeTime;
    @ApiModelProperty("未读数")
    private Integer unreadCount;
}

package com.hitsz.badboyChat.common.chat.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/27 14:55
 */
@Data
public class RoomBaseInfo {
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
    @ApiModelProperty("最新活跃时间")
    private Date activeTime;
    @ApiModelProperty("最新消息id")
    private Long lastMsgId;
}

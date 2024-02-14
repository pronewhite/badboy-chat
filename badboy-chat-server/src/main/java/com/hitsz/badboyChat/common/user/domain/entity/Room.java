package com.hitsz.badboyChat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hitsz.badboyChat.common.enums.RoomHotFlagEnum;
import com.hitsz.badboyChat.common.enums.RoomTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 房间表
 * @TableName room
 */
@TableName(value ="room")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 房间类型 1群聊 2单聊
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 是否全员展示 0否 1是
     */
    @TableField(value = "hot_flag")
    private Integer hotFlag;

    /**
     * 群最后消息的更新时间（热点群不需要写扩散，只更新这里）
     */
    @TableField(value = "active_time")
    private Date activeTime;

    /**
     * 会话中的最后一条消息id
     */
    @TableField(value = "last_msg_id")
    private Long lastMsgId;

    /**
     * 额外信息（根据不同类型房间有不同存储的东西）
     */
    @TableField(value = "ext_json")
    private Object extJson;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除 0未删除 1已删除
     */
    @TableField(value = "is_deleted")
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public boolean isHotRoom(){
        return RoomHotFlagEnum.of(this.hotFlag) == RoomHotFlagEnum.YES;
    }

    public boolean isRoomFriend(){
        return RoomTypeEnum.of(this.type) == RoomTypeEnum.SINGLE_CHAT;
    }

    public boolean isRoomGroup(){
        return RoomTypeEnum.of(this.type)== RoomTypeEnum.GROUP_CHAT;
    }
}
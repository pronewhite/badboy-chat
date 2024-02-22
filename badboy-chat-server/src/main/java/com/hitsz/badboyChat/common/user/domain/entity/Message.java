package com.hitsz.badboyChat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hitsz.badboyChat.common.chat.domain.entity.msg.MessageExtra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息表
 * @TableName message
 */
@TableName(value ="message")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话表id
     */
    @TableField(value = "room_id")
    private Long roomId;

    /**
     * 消息发送者uid
     */
    @TableField(value = "from_uid")
    private Long fromUid;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 回复的消息内容
     */
    @TableField(value = "reply_msg_id")
    private Long replyMsgId;

    /**
     * 消息状态 0正常 1删除
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 与回复的消息间隔多少条
     */
    @TableField(value = "gap_count")
    private Integer gapCount;

    /**
     * 消息类型 1正常文本 2.撤回消息
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 扩展信息
     */
    @TableField(value = "extra",typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private MessageExtra extra;

    /**
     * 是否删除 0未删除 1已删除
     */
    @TableField(value = "is_deleted")
    @TableLogic
    private Integer isDeleted;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.hitsz.badboyChat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户申请表
 * @TableName user_apply
 */
@TableName(value ="user_apply")
@Data
public class UserApply implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 申请人uid
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 申请类型 1加好友
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 接收人uid
     */
    @TableField(value = "target_id")
    private Long targetId;

    /**
     * 申请信息
     */
    @TableField(value = "msg")
    private String msg;

    /**
     * 申请状态 1待审批 2同意
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 阅读状态 1未读 2已读
     */
    @TableField(value = "read_status")
    private Integer readStatus;

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

    @TableField(value = "is_deleted")
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.hitsz.badboyChat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群成员表
 * @TableName group_member
 */
@TableName(value ="group_member")
@Data
public class GroupMember implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群主id
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 成员uid
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 成员角色 1群主 2管理员 3普通成员
     */
    @TableField(value = "role")
    private Integer role;

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
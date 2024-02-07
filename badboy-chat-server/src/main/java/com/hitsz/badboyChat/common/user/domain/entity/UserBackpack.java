package com.hitsz.badboyChat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户背包表
 * @TableName user_backpack
 */
@TableName(value ="user_backpack")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBackpack implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uid
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 物品id
     */
    @TableField(value = "item_id")
    private Long itemId;

    /**
     * 使用状态 0.待使用 1已使用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 幂等号
     */
    @TableField(value = "idempotent")
    private String idempotent;

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
}
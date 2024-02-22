package com.hitsz.badboyChat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表情包
 * @TableName user_emoji
 */
@TableName(value ="user_emoji")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEmoji implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户表ID
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 表情地址
     */
    @TableField(value = "expression_url")
    private String expressionUrl;

    /**
     * 逻辑删除(0-正常,1-删除)
     */
    @TableField(value = "delete_status")
    @TableLogic(value = "0", delval = "1")
    private Integer deleteStatus;

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
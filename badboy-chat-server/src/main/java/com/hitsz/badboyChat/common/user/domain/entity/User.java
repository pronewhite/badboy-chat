package com.hitsz.badboyChat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user", autoResultMap = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 用户头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 性别 1为男性，2为女性
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 微信openid用户标识
     */
    @TableField(value = "open_id")
    private String openId;

    /**
     * 在线状态 1在线 2离线
     */
    @TableField(value = "active_status")
    private Integer activeStatus;

    /**
     * 最后上下线时间
     */
    @TableField(value = "last_opt_time")
    private Date lastOptTime;

    /**
     * ip信息
     */
    @TableField(value = "ip_info",typeHandler = JacksonTypeHandler.class)
    private IpInfo ipInfo;

    /**
     * 佩戴的徽章id
     */
    @TableField(value = "item_id")
    private Long itemId;

    /**
     * 使用状态 0.正常 1拉黑
     */
    @TableField(value = "status")
    private Integer status;

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
     * 逻辑删除
     */
    @TableField(value = "is_deleted")
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public void refreshIp(String ip){
        if(ipInfo == null){
            ipInfo = new IpInfo();
        }
        ipInfo.refreshIp(ip);
    }
}
package com.hitsz.badboychat.transaction.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hitsz.badboychat.transaction.domain.dto.SecureInvokeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 本地消息表
 * @TableName secure_invoke_record
 */
@TableName(value ="secure_invoke_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecureInvokeRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 请求快照参数json
     */
    @TableField(value = "secure_invoke_json", typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private SecureInvokeDTO secureInvokeDTO;

    /**
     * 状态 1待执行 2已失败
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 下一次重试的时间
     */
    @TableField(value = "next_retry_time")
    private Date nextRetryTime;

    /**
     * 已经重试的次数
     */
    @TableField(value = "retry_times")
    private Integer retryTimes;

    /**
     * 最大重试次数
     */
    @TableField(value = "max_retry_times")
    private Integer maxRetryTimes;

    /**
     * 执行失败的堆栈
     */
    @TableField(value = "fail_reason")
    private String failReason;

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
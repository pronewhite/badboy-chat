package com.hitsz.badboyChat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName sensitive_word
 */
@TableName(value ="sensitive_word")
@Data
public class SensitiveWord implements Serializable {
    /**
     * 
     */
    @TableField(value = "word")
    private String word;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
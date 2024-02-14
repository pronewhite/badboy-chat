package com.hitsz.badboyChat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/2 16:53
 */
@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum{

    LOCK_LIMIT(1001,"请求过于频繁，请稍后再试"), PARAM_INVALID(1002, "参数不合法");

    public Integer type;
    public String desc;
    @Override
    public Integer getErrorCode() {
        return type;
    }

    @Override
    public String getErrorMessage() {
        return desc;
    }
}

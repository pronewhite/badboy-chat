package com.hitsz.badboyChat.common.exception;

import com.hitsz.badboyChat.common.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 20:12
 */
@AllArgsConstructor
public enum ExceptionResponseEnum implements ErrorEnum {
    SYSTEM_ERROR(500, "系统错误"),
    PARAM_INVALID(-2, "参数有误"),
    BUISSNESS_EXCEPTION(0,"{0}");

    private Integer errCode;
    private String errMsg;

    @Override
    public Integer getErrorCode() {
        return errCode;
    }

    @Override
    public String getErrorMessage() {
        return errMsg;
    }
}

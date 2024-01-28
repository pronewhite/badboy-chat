package com.hitsz.badboyChat.common.exception;

import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 20:59
 */
@Data
public class BusinessException extends RuntimeException{

    public Integer code;
    public String msg;

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String msg){
        super(msg);
        this.code = ExceptionResponseEnum.BUISSNESS_EXCEPTION.getErrorCode();
        this.msg = msg;
    }
}
